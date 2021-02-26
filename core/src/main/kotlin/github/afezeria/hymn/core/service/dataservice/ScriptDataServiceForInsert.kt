package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.exception.PermissionDeniedException
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.platform.dataservice.FieldInfo
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.toJson
import java.time.LocalDateTime

/**
 * @author afezeria
 */
interface ScriptDataServiceForInsert : ScriptDataServiceForQuery {
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
        if (dataList.isEmpty()) throw BusinessException("插入数据不能为空")
        if (dataList.size > 100) throw BusinessException("批量插入数据一次不能大于100条")

        getObjectByApi(objectApiName) ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        val session = Session.getInstance()
        val roleId = session.roleId

        val fields: MutableList<FieldInfo> = getFieldApiMap(objectApiName)
            .mapNotNullTo(mutableListOf()) { if (it.value.type != "auto" && it.value.type != "summary") it.value else null }
        val typeIdSet = getTypeList(objectApiName).mapTo(mutableListOf()) { it.id }
        if (withPerm) {
            val objectPerm = getObjectPerm(roleId, objectApiName)
                ?: throw PermissionDeniedException("缺少对象 [api:$objectApiName] 新建权限")
            if (!objectPerm.ins) {
                throw PermissionDeniedException("缺少对象 [api:$objectApiName] 新建权限")
            }
            val writeableFieldApiSet = getFieldApiSetWithPerm(roleId, objectApiName, edit = true)
            fields.removeIf { !writeableFieldApiSet.contains(it.api) }
            val visibleTypeIdSet = getVisibleTypeIdSet(roleId, objectApiName)
            typeIdSet.removeIf { !visibleTypeIdSet.contains(it) }
        }


        val insertDataList = mutableListOf<LinkedHashMap<String, Any?>>()
        for (data in dataList) {
            val typeId = data["type_id"] as String?
            if (typeId == null) {
                logger.info("$objectApiName data:${data.toJson()}")
                throw BusinessException("新增数据未指定业务类型")
            }
            if (!typeIdSet.contains(typeId)) {
                throw BusinessException("业务类型 [id:$typeId] 不存在或缺少权限")
            }

            val insertData = LinkedHashMap<String, Any?>()
            val now = LocalDateTime.now()
            for (field in fields) {
                insertData[field.api] =
                    checkNewDataValue(field, data[field.api], session, now)
            }
            insertDataList.add(insertData)
        }

        return insertDataList.mapTo(ArrayList()) { insertHelper(objectApiName, it) }
    }

    private fun insertHelper(
        objectApiName: String,
        new: LinkedHashMap<String, Any?>,
    ): MutableMap<String, Any?> {

        val columns = new.keys.joinToString("\",\"", "\"", "\"")
        val placeholder = "?" + ",?".repeat(new.keys.size - 1)
        val returnColumns = "\"id\"" + new.keys.joinToString("\",\"", ",\"", "\"")

        //language=PostgreSQL
        val sql = """
            insert into hymn_view.$objectApiName ($columns) values ($placeholder) 
            returning $returnColumns
        """
        return execute(sql, new.values, WriteType.INSERT, objectApiName, null, new, true)
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
        if (dataList.isEmpty()) throw BusinessException("插入数据不能为空")
        if (dataList.size > 100) throw BusinessException("批量插入数据一次不能大于100条")

        getObjectByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        val session = Session.getInstance()
        val now = LocalDateTime.now()

        val fieldMap = getFieldApiMap(objectApiName)
            .filter { it.value.type != "auto" && it.value.type != "summary" }


        val insertDataList = mutableListOf<LinkedHashMap<String, Any?>>()
        for (data in dataList) {
            val typeId = data["type_id"] as String?
            if (typeId == null) {
                logger.info("$objectApiName data:${data.toJson()}")
                throw BusinessException("新增数据未指定业务类型")
            }

            val insertData = LinkedHashMap<String, Any?>()
            for (field in fieldMap.values) {
                insertData[field.api] = checkNewDataValue(field, data[field.api], session, now)
            }
            insertDataList.add(insertData)
        }


        val columns = fieldMap.keys.joinToString(", ")
        val rowPlaceholder = "(?${",?".repeat(fieldMap.size - 1)}),"
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
                        insert into hymn_view.$objectApiName ($columns)
                        values $ph
                        returning id,$columns
                    """
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