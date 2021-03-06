package com.github.afezeria.hymn.core.platform.dataservice

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.exception.PermissionDeniedException
import com.github.afezeria.hymn.common.platform.Session
import java.time.LocalDateTime

/**
 * @author afezeria
 */
interface ScriptDataServiceForUpdate : ScriptDataService {
    override fun update(
        objectApiName: String,
        data: MutableMap<String, Any?>,
    ): MutableMap<String, Any?> {
        return batchUpdate(objectApiName, listOf(data), withPerm = false, trigger = true).first()
    }

    override fun batchUpdate(
        objectApiName: String,
        dataList: MutableList<MutableMap<String, Any?>>,
    ): MutableList<MutableMap<String, Any?>> {
        return batchUpdate(objectApiName, dataList, withPerm = false, trigger = true)
    }

    override fun updateWithPerm(
        objectApiName: String,
        data: MutableMap<String, Any?>
    ): MutableMap<String, Any?> {
        return batchUpdate(objectApiName, listOf(data), withPerm = true, trigger = true).first()
    }

    override fun batchUpdateWithPerm(
        objectApiName: String,
        dataList: MutableList<MutableMap<String, Any?>>
    ): MutableList<MutableMap<String, Any?>> {
        return batchUpdate(objectApiName, dataList, withPerm = true, trigger = true)
    }

    override fun updateWithoutTrigger(
        objectApiName: String,
        data: MutableMap<String, Any?>
    ): MutableMap<String, Any?> {
        return batchUpdate(objectApiName, listOf(data), withPerm = false, trigger = false).first()
    }

    override fun batchUpdateWithoutTrigger(
        objectApiName: String,
        dataList: MutableList<MutableMap<String, Any?>>
    ): MutableList<MutableMap<String, Any?>> {
        return batchUpdate(objectApiName, dataList, withPerm = false, trigger = false)
    }

    fun batchUpdate(
        objectApiName: String,
        dataList: List<Map<String, Any?>>,
        withPerm: Boolean,
        trigger: Boolean,
    ): MutableList<MutableMap<String, Any?>> {
        val objectInfo =
            getObjectByApi(objectApiName) ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        if (objectInfo.type != "custom" && objectInfo.canUpdate != true) {
            throw BusinessException("对象 [objectApiName:$objectApiName] 不支持更新操作")
        }

        val session = Session.getInstance()
        val roleId = session.roleId
        val fieldApiMap = getFieldApiMap(objectApiName)

//
        val updateDataMap = mutableMapOf<String, Map<String, Any?>>()
        val ids = mutableSetOf<String>()
        for (map in dataList) {
            val id = map["id"]
            if (id is String) {
                ids.add(id)
                updateDataMap[id] = map
            }
        }
        if (ids.size != dataList.size) {
            throw BusinessException("未指定更新数据的id")
        }

        var readableFieldApiSet: Set<String>? = null
        val oldAndNewData =
            mutableListOf<Pair<MutableMap<String, Any?>, NewRecordMap>>()
        var oldDataList: MutableList<MutableMap<String, Any?>>
        val writeableFieldApiSet: Set<String>
        val visibleTypeIdSet: Set<String>
        if (withPerm) {
            val objectPerm = getObjectPerm(objectApiName, roleId)
                ?: throw PermissionDeniedException("缺少对象 [api:$objectApiName] 更新权限")
            if (!objectPerm.upd) {
                throw PermissionDeniedException("缺少对象 [api:$objectApiName] 更新权限")
            }

            writeableFieldApiSet = getFieldApiSetWithPerm(objectApiName, roleId, edit = true)
            if (writeableFieldApiSet.isEmpty()) {
                throw PermissionDeniedException("缺少对象 [api:$objectApiName] 任意字段的编辑权限")
            }
            readableFieldApiSet = getFieldApiSetWithPerm(objectApiName, roleId, read = true)
            visibleTypeIdSet = getVisibleTypeIdSet(objectApiName, roleId)
            oldDataList = queryWithPerm(
                objectApiName, "id = any (?)", ids,
            )
//            判断是否有更新权限
//            有对象的编辑全部权限则不过滤数据
            if (!objectPerm.editAll) {
                val map = oldDataList.groupBy { it["owner_id"] == session.accountId }
                val ownData = map[true]
                var otherAccountsData = map[false]?.toMutableList()
                if (otherAccountsData != null) {
//                    如果待更新数据中有其他人的数据则检查是否有被共享编辑权限
                    if (objectInfo.type == "custom") {
                        val updatableDataIdSet = getShare(
                            objectApiName,
                            otherAccountsData.map { it["id"] as String },
                            session.roleId,
                            session.orgId,
                            session.accountId,
                            false
                        ).mapTo(mutableSetOf()) { it.dataId }
                        otherAccountsData.removeAll { !updatableDataIdSet.contains(it["id"]) }
                    } else {
//                        只有自定义对象能被共享，
//                        当前对象不是自定义对象则将属于其他帐号的待更新数据移除
                        otherAccountsData = null
                    }
                }
                oldDataList = mutableListOf()
                oldDataList.addAll(ownData ?: emptyList())
                oldDataList.addAll(otherAccountsData ?: emptyList())
            }
        } else {
            oldDataList = queryByIds(objectApiName, ids)
            visibleTypeIdSet = getTypeList(objectApiName).mapTo(mutableSetOf()) { it.id }
            writeableFieldApiSet = fieldApiMap.keys
        }
        for (old in oldDataList) {
            val id = old["id"] as String
            updateDataMap[id]?.apply {
                val insertData = NewRecordMap(fieldApiMap)
                val now = LocalDateTime.now()
                for ((api, field) in fieldApiMap) {
                    if (writeableFieldApiSet.contains(api) && containsKey(api)) {
                        if (api == "type_id") {
//                            确保业务类型id不为null
                            val typeId = get("type_id")
                            if (typeId == null) {
                                insertData[api] = old[api]
                            } else {
                                if (!visibleTypeIdSet.contains(typeId)) {
                                    throw BusinessException("业务类型 [id:$typeId] 不存在或缺少权限")
                                }
                                insertData["type_id"] = typeId
                            }
                        } else {
                            var any = get(api)
//                            处理标准字段
                            if (field.predefined) {
                                any = when (field.standardType) {
                                    "create_by_id" -> any
                                    "modify_by_id" -> session.accountId
                                    "create_date" -> any
                                    "modify_date" -> now
                                    "org_id" -> any
                                    "lock_state" -> any
                                    "name" -> any
                                    "type_id" -> any
                                    "owner_id" -> any
                                    else -> throw InnerException("错误的标准字段类型")
                                }
                            }
                            insertData[api] = any
                        }
                    } else {
                        insertData[api] = old[api]
                    }
                }
                insertData["id"] = id
                oldAndNewData.add(old to insertData)
            }
        }

        return oldAndNewData.mapTo(ArrayList()) {
            updateHelper(
                objectApiName,
                it.first,
                it.second,
                readableFieldApiSet,
                trigger
            )
        }
    }

    private fun updateHelper(
        objectApiName: String,
        old: MutableMap<String, Any?>,
        new: NewRecordMap,
        readableFieldApiSet: Set<String>? = null,
        trigger: Boolean = true,
    ): MutableMap<String, Any?> {
        val before = { _: Map<String, Any?>?,
                       newData: MutableMap<String, Any?>? ->
            requireNotNull(newData)

//            newData是LinkHashMap的实例，填充newData变量数据的时候id是最后一个填入的
//            所以截断最后一个逗号之前的字符串来避免id出现在set语句中
            val setStr = newData.keys.joinToString(", ") { "$it = ?" }.substringBeforeLast(",")
            //language=PostgreSQL
            val sql = """
                update hymn_view."$objectApiName" 
                set $setStr
                where id = ?
                returning *
            """
            sql to newData.values
        }
        val after: (MutableMap<String, Any?>?) -> Pair<MutableMap<String, Any?>, MutableMap<String, Any?>?> =
            { returning ->
                requireNotNull(returning)
                if (readableFieldApiSet == null) {
                    LinkedHashMap(returning)
                } else {
                    returning.filterTo(mutableMapOf()) { readableFieldApiSet.contains(it.key) }
                } to returning
            }
        return execute(
            beforeExecutingSql = before,
            afterExecutingSql = after,
            type = WriteType.UPDATE,
            objectApiName = objectApiName,
            oldData = old,
            newData = new,
            withTrigger = trigger
        )
    }

}