package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.exception.PermissionDeniedException
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.util.execute
import java.time.LocalDateTime

/**
 * @author afezeria
 */
interface ScriptDataServiceForUpdate : ScriptDataServiceForQuery {
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

        val session = Session.getInstance()
        val roleId = session.roleId
        val fieldMap = getFieldApiMap(objectApiName)

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
            mutableListOf<Pair<MutableMap<String, Any?>, LinkedHashMap<String, Any?>>>()
        if (withPerm) {
            val objectPerm = getObjectPerm(roleId, objectApiName)
                ?: throw PermissionDeniedException("缺少对象 [api:$objectApiName] 更新权限")
            if (!objectPerm.upd) {
                throw PermissionDeniedException("缺少对象 [api:$objectApiName] 更新权限")
            }

            val writeableFieldApiSet = getFieldApiSetWithPerm(roleId, objectApiName, edit = true)
            readableFieldApiSet = getFieldApiSetWithPerm(roleId, objectApiName, read = true)
            val visibleTypeIdSet = getVisibleTypeIdSet(roleId, objectApiName)
            val oldDataList =
                queryWithPerm(
                    objectApiName, "id = any (?)", ids,
                    fieldSet = emptySet(),
                )
            if (objectInfo.type == "custom") {
                val updatableDataIdSet =
                    database.useConnection {
                        //language=PostgreSQL
                        val sql = """
                            select data_id from hymn_view."${objectApiName}_share" 
                            where read_only = false and data_id = any (?) and (role_id = ? or account_id = ? or org_id = ?)
                        """
                        it.execute(sql, ids, session.roleId, session.accountId, session.orgId)
                            .map { it["data_id"] as String }.toSet()
                    }
                oldDataList.removeIf { !updatableDataIdSet.contains(it["id"]) }
            } else {
                if (!objectPerm.editAll) {
                    oldDataList.removeIf { it["owner_id"] == session.accountId }
                }
            }


            for (old in oldDataList) {
                val id = old["id"] as String
                updateDataMap[id]?.apply {
                    val typeId = get("type_id") as String
                    if (!visibleTypeIdSet.contains(typeId)) {
                        throw PermissionDeniedException("缺少 [id:$typeId] 业务类型权限")
                    }
                    val insertData = LinkedHashMap<String, Any?>()
                    val now = LocalDateTime.now()
                    for ((api, field) in fieldMap) {
                        if (writeableFieldApiSet.contains(api) && containsKey(api)) {
                            insertData[api] =
                                checkNewDataValue(field, get(api), session, now)
                        } else {
                            insertData[api] = old[api]
                        }
                    }
                    insertData["id"] = id
                    oldAndNewData.add(old to insertData)
                }
            }
        } else {
            val oldDataList = queryByIds(objectApiName, ids)
            for (old in oldDataList) {
                val id = old["id"] as String
                updateDataMap[id]?.apply {
                    if (containsKey("type_id") && get("type_id") == null) {
                        throw BusinessException("不能将 type_id 更新为null")
                    }
                    val insertData = LinkedHashMap<String, Any?>()
                    val now = LocalDateTime.now()
                    for ((api, field) in fieldMap) {
                        if (containsKey(api)) {
                            insertData[api] = checkNewDataValue(field, get(api), session, now)
                        } else {
                            insertData[api] = old[api]
                        }
                    }
                    insertData["id"] = id
                    oldAndNewData.add(old to insertData)
                }
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
        new: LinkedHashMap<String, Any?>,
        fields: Collection<String>? = null,
        trigger: Boolean = true,
    ): MutableMap<String, Any?> {

        val returnColumns = if (fields == null) {
            new.keys.joinToString("\",\"", ",\"", "\"")
        } else {
            "\"id\"" + fields.joinToString("\",\"", ",\"", "\"")
        }

//        id字段在最后一个
        val setStr = new.keys.joinToString(", ") { "$it = ?" }.substringBeforeLast(",")
        //language=PostgreSQL
        val sql = """
            update hymn_view."$objectApiName" 
            set $setStr
            where id = ?
            returning $returnColumns
        """
        return execute(sql, new.values, WriteType.UPDATE, objectApiName, old, new, trigger)
    }

}