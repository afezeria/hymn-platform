package com.github.afezeria.hymn.core.platform.dataservice

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.exception.PermissionDeniedException
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.common.platform.dataservice.FieldInfo
import com.github.afezeria.hymn.common.util.execute
import com.github.afezeria.hymn.common.util.toJson
import java.time.LocalDateTime

/**
 * @author afezeria
 */
interface ScriptDataServiceForInsert : ScriptDataService {
    override fun insert(
        objectApiName: String,
        data: Map<String, Any?>,
    ): MutableMap<String, Any?> {
        return batchInsert(objectApiName, listOf(data)).first()
    }

    override fun batchInsert(
        objectApiName: String,
        dataList: List<Map<String, Any?>>
    ): MutableList<MutableMap<String, Any?>> {
        return batchInsert(objectApiName, dataList, false)
    }


    override fun insertWithPerm(
        objectApiName: String,
        data: Map<String, Any?>
    ): MutableMap<String, Any?> {
        return batchInsert(objectApiName, listOf(data), true).first()
    }

    override fun batchInsertWithPerm(
        objectApiName: String,
        dataList: List<Map<String, Any?>>,
    ): MutableList<MutableMap<String, Any?>> {
        return batchInsert(objectApiName, dataList, true)
    }

    private fun batchInsert(
        objectApiName: String,
        dataList: List<Map<String, Any?>>,
        withPerm: Boolean = true,
    ): MutableList<MutableMap<String, Any?>> {
        if (dataList.isEmpty()) return mutableListOf()
        if (dataList.size > 100) throw BusinessException("批量插入数据一次不能大于100条")

        val objectInfo =
            getObjectByApi(objectApiName) ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        if (objectInfo.type != "custom" && objectInfo.canInsert != true) {
            throw BusinessException("对象 [objectApiName:$objectApiName] 不支持新增操作")
        }

        val session = Session.getInstance()
        val roleId = session.roleId

        val fieldApiMap = getFieldApiMap(objectApiName)
        val fields: MutableList<FieldInfo> = fieldApiMap
            .values.filterTo(ArrayList()) { it.type != "auto" && it.type != "summary" }

        val typeIdSet = getTypeList(objectApiName).mapTo(mutableListOf()) { it.id }
        var readableFieldApiSet: Set<String>? = null
        if (withPerm) {
            val objectPerm = getObjectPerm(roleId, objectApiName)
                ?: throw PermissionDeniedException("缺少对象 [api:$objectApiName] 新建权限")
            if (!objectPerm.ins) {
                throw PermissionDeniedException("缺少对象 [api:$objectApiName] 新建权限")
            }
            val writeableFieldApiSet = getFieldApiSetWithPerm(roleId, objectApiName, edit = true)
            fields.removeAll { !writeableFieldApiSet.contains(it.api) }
            val visibleTypeIdSet = getVisibleTypeIdSet(roleId, objectApiName)
            typeIdSet.removeAll { !visibleTypeIdSet.contains(it) }

            readableFieldApiSet = getFieldApiSetWithPerm(roleId, objectApiName, read = true)
        }


        val insertDataList = mutableListOf<NewRecordMap>()
        val now = LocalDateTime.now()
        for (data in dataList) {
            if (data.isEmpty()) {
                continue
            }
            val typeId = data["type_id"] as String?
            if (typeId == null) {
                logger.info("$objectApiName data:${data.toJson()}")
                throw BusinessException("新增数据未指定业务类型")
            }
            val name = data["name"] as String?
            if (name == null) {
                val nameField = requireNotNull(fieldApiMap["name"])
                logger.info("$objectApiName data:${data.toJson()}")
                throw BusinessException("新增数据未指定 ${nameField.name}")
            }
            if (!typeIdSet.contains(typeId)) {
                throw BusinessException("业务类型 [id:$typeId] 不存在或缺少权限")
            }

            val insertData = NewRecordMap(fieldApiMap)
            for (field in fields) {
                var any = data[field.api]
                if (field.predefined && field.standardType != null) {
                    any = when (field.standardType) {
                        "create_by_id" -> session.accountId
                        "modify_by_id" -> session.accountId
                        "create_date" -> now
                        "modify_date" -> now
                        "org_id" -> session.orgId
                        "lock_state" -> false
                        "name" -> any
                        "type_id" -> any
                        "owner_id" -> session.accountId
                        else -> throw InnerException("错误的标准字段类型")
                    }
                }
                insertData[field.api] = any
            }
            insertDataList.add(insertData)
        }

        return insertDataList.mapTo(ArrayList()) {
            insertHelper(
                objectApiName,
                it,
                readableFieldApiSet
            )
        }
    }

    private fun insertHelper(
        objectApiName: String,
        new: NewRecordMap,
        readableFieldApiSet: Set<String>?,
    ): MutableMap<String, Any?> {
        val before = { _: Map<String, Any?>?,
                       newData: MutableMap<String, Any?>? ->
            requireNotNull(newData)
            val columns = newData.keys.joinToString("\",\"", "\"", "\"")
            val placeholder = "?" + ",?".repeat(newData.keys.size - 1)

            //language=PostgreSQL
            val sql = """
                insert into hymn_view."$objectApiName" ($columns) values ($placeholder) 
                returning id
            """
            sql to newData.values
        }
        val after: (MutableMap<String, Any?>?) -> Pair<MutableMap<String, Any?>, MutableMap<String, Any?>?> =
            {
                val newDataId = requireNotNull(it?.get("id")) as String
                val newData = database.useConnection {
                    it.execute(
                        """
                        select * from hymn_view."$objectApiName" where id = ?
                    """, newDataId
                    ).firstOrNull()
                }
                requireNotNull(newData)
                if (readableFieldApiSet == null) {
                    LinkedHashMap(newData)
                } else {
                    newData.filterTo(mutableMapOf()) { readableFieldApiSet.contains(it.key) }
                } to newData
            }
        return execute(
            beforeExecutingSql = before,
            afterExecutingSql = after,
            type = WriteType.INSERT,
            objectApiName = objectApiName,
            oldData = null,
            newData = new,
            withTrigger = true
        )
    }

    override fun insertWithoutTrigger(
        objectApiName: String,
        data: MutableMap<String, Any?>,
    ): MutableMap<String, Any?> {
        return bulkInsertWithoutTrigger(objectApiName, listOf(data)).first()
    }

    override fun bulkInsertWithoutTrigger(
        objectApiName: String,
        dataList: List<Map<String, Any?>>,
    ): MutableList<MutableMap<String, Any?>> {
        if (dataList.isEmpty()) return mutableListOf()
        if (dataList.size > 500) throw BusinessException("批量插入数据一次不能大于500条")

        val objectInfo = (getObjectByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]"))
        if (objectInfo.type != "custom" && objectInfo.canInsert != true) {
            throw BusinessException("对象 [objectApiName:$objectApiName] 不支持新增操作")
        }

        val session = Session.getInstance()

        val fieldMap = getFieldApiMap(objectApiName)

        val insertDataList = mutableListOf<LinkedHashMap<String, Any?>>()
        val now = LocalDateTime.now()
        for (data in dataList) {
            if (data.isEmpty()) continue
            val typeId = data["type_id"] as String?
            if (typeId == null) {
                logger.info("$objectApiName data:${data.toJson()}")
                throw BusinessException("新增数据未指定业务类型")
            }

            val insertData = NewRecordMap(fieldMap)
            for (field in fieldMap.values) {
                var any = data[field.api]
                if (field.predefined && field.standardType != null) {
                    any = when (field.standardType) {
                        "create_by_id" -> session.accountId
                        "modify_by_id" -> session.accountId
                        "create_date" -> now
                        "modify_date" -> now
                        "org_id" -> session.orgId
                        "lock_state" -> false
                        "name" -> any
                        "type_id" -> any
                        "owner_id" -> session.accountId
                        else -> throw InnerException("错误的标准字段类型")
                    }
                }
                insertData[field.api] = any
            }

            insertDataList.add(insertData)
        }


        val insertFields = fieldMap.values.filter { it.type != "summary" && it.type != "auto" }
        val insertColumns = insertFields.joinToString("\",\"", "\"", "\"")
        val returnColumns = fieldMap.values.filter { it.type != "summary" }

        val rowPlaceholder = "(?${",?".repeat(insertFields.size - 1)}),"
        var idx = 0
        var batchSize = 50
        val size = insertDataList.size
        var lastBatchSize = 50
        var sql: String? = null
        val result = mutableListOf<MutableMap<String, Any?>>()
        database.useConnection {
            while (idx < size) {
                if (idx + batchSize > size) {
                    batchSize = size - idx
                }
                if (sql == null || batchSize != lastBatchSize) {
                    val ph = rowPlaceholder.repeat(batchSize).substringBeforeLast(",")
                    //language=PostgreSQL
                    sql = """
                        insert into hymn_view.$objectApiName ($insertColumns)
                        values $ph
                        returning id,$returnColumns
                    """.trimIndent()
                }
                val list = mutableListOf<Any?>()
                for (map in insertDataList.subList(idx, idx + batchSize)) {
                    for (entry in map) {
                        list.add(entry.value)
                    }
                }
                val execute = it.execute(sql!!, list)
                result.addAll(execute)
                lastBatchSize = batchSize
                idx += batchSize
            }
        }
        return result
    }

}