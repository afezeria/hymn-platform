package com.github.afezeria.hymn.core.service.dataservice

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.exception.PermissionDeniedException
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.common.util.execute

/**
 * @author afezeria
 */
interface ScriptDataServiceForDelete : ScriptDataService {

    override fun delete(
        objectApiName: String,
        id: String,
    ): MutableMap<String, Any?>? {
        return batchDelete(objectApiName, listOf(id), false).firstOrNull()
    }

    override fun batchDelete(
        objectApiName: String,
        ids: MutableList<String>
    ): MutableList<MutableMap<String, Any?>> {
        return batchDelete(objectApiName, ids, false)
    }

    override fun deleteWithPerm(objectApiName: String, id: String): MutableMap<String, Any?>? {
        return batchDelete(objectApiName, listOf(id), true).firstOrNull()
    }

    override fun batchDeleteWithPerm(
        objectApiName: String,
        ids: MutableList<String>
    ): MutableList<MutableMap<String, Any?>> {
        return batchDelete(objectApiName, ids, true)
    }

    override fun deleteWithoutTrigger(
        objectApiName: String,
        id: String
    ): MutableMap<String, Any?>? {
        return batchDeleteWithoutTrigger(objectApiName, listOf(id)).firstOrNull()
    }

    override fun batchDeleteWithoutTrigger(
        objectApiName: String,
        ids: List<String>
    ): MutableList<MutableMap<String, Any?>> {
        val objectInfo = getObjectByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        if (objectInfo.type != "custom" && objectInfo.canDelete != true) {
            throw BusinessException("对象 [objectApiName:$objectApiName] 不支持删除操作")
        }

        if (ids.isEmpty()) return mutableListOf()

        val result = database.useConnection {
            it.execute(
                """
                delete from hymn_view."$objectApiName" where id = any (?)
                returning *
            """, ids
            )
        }
        for (map in result) {
            val id = requireNotNull(map["id"]) as String
            processingDeletePolicy(objectApiName, id, false)
        }
        return result
    }


    private fun batchDelete(
        objectApiName: String,
        ids: List<String>,
        withPerm: Boolean,
    ): MutableList<MutableMap<String, Any?>> {
        if (ids.isEmpty()) return mutableListOf()
        if (ids.size > 100) throw BusinessException("批量删除数据一次不能大于100条")

        val objectInfo =
            getObjectByApi(objectApiName) ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        if (objectInfo.type != "custom" && objectInfo.canDelete != true) {
            throw BusinessException("对象 [objectApiName:$objectApiName] 不支持删除操作")
        }

        var oldDataList: MutableList<MutableMap<String, Any?>>

        var readableFieldApiSet: Set<String>? = null
        if (withPerm) {
            val session = Session.getInstance()
            val objectPerm = getObjectPerm(session.roleId, objectApiName)
                ?: throw PermissionDeniedException("缺少对象 [api:$objectApiName] 删除权限")
            if (!objectPerm.del) {
                throw PermissionDeniedException("缺少对象 [api:$objectApiName] 删除权限")
            }
            oldDataList = queryWithPerm(
                objectApiName, "id = any (?)", ids,
            )
            if (!objectPerm.editAll) {
//                如果没有对象的编辑全部权限则过滤掉所有人不是当前用户的数据
                oldDataList = oldDataList.filterTo(ArrayList()) {
                    it["owner_id"] == session.accountId
                }
            }
            readableFieldApiSet =
                getFieldApiSetWithPerm(Session.getInstance().roleId, objectApiName, read = true)
        } else {
            oldDataList = queryByIds(objectApiName, ids)
        }
        if (oldDataList.isEmpty()) return mutableListOf()

        return oldDataList.mapTo(ArrayList()) {
            deleteHelper(
                objectApiName,
                it,
                readableFieldApiSet
            )
        }
    }

    private fun deleteHelper(
        objectApiName: String,
        old: MutableMap<String, Any?>,
        readableFieldApiSet: Set<String>?,
    ): MutableMap<String, Any?> {
        val before = { _: Map<String, Any?>?,
                       _: MutableMap<String, Any?>? ->
            //language=PostgreSQL
            val sql =
                """
                delete from hymn_view."$objectApiName" where id = ? 
                returning *
            """
            sql to listOf(old["id"])
        }
        val after: (MutableMap<String, Any?>?) -> Pair<MutableMap<String, Any?>, MutableMap<String, Any?>?> =
            { returning ->
                requireNotNull(returning)
                val masterId = requireNotNull(returning["id"]) as String
                processingDeletePolicy(objectApiName, masterId, true)
                if (readableFieldApiSet == null) {
                    returning
                } else {
                    returning.filterTo(mutableMapOf()) { readableFieldApiSet.contains(it.key) }
                } to null
            }
        return execute(
            beforeExecutingSql = before,
            afterExecutingSql = after,
            type = WriteType.DELETE,
            objectApiName = objectApiName,
            oldData = old,
            newData = null,
            withTrigger = true
        )
    }

    /**
     * 处理删除时的级联和阻止动作
     */
    private fun processingDeletePolicy(
        objectApiName: String,
        masterDataId: String,
        withTrigger: Boolean
    ) {
        val refFieldList = getFieldByRefObjectId(objectApiName)
        if (refFieldList.isEmpty()) return

        for (fieldInfo in refFieldList) {
            if (fieldInfo.refDeletePolicy == "restrict") {
//                处理主从和关联字段的阻止删除动作
                if (fieldInfo.type == "master_slave" || fieldInfo.type == "reference") {
                    val refObjectInfo = requireNotNull(getObjectById(fieldInfo.objectId))
                    val count =
                        count(refObjectInfo.api, "\"${fieldInfo.api}\" = ?", listOf(masterDataId))
                    if (count > 0) {
                        throw BusinessException("删除失败，当前数据被${refObjectInfo.name}对象的数据引用，请删除所有引用数据后再执行删除操作")
                    }
                } else if (fieldInfo.type == "mreference") {
//                    处理多选关联字段的阻止删除动作
                    val refObjectInfo = requireNotNull(getObjectById(fieldInfo.objectId))
                    //language=PostgreSQL
                    var count = 0
                    database.useConnection {
                        it.execute(
                            """
                            select count(*) count from hymn_view."join_${refObjectInfo.api}_${fieldInfo.api}" where t_id = ?;
                        """, masterDataId
                        ) { it?.apply { if (next()) count = getInt(1) } }
                    }
                    if (count > 0) {
                        throw BusinessException("删除失败，当前数据被${refObjectInfo.name}对象的数据引用，请删除所有引用数据后再执行删除操作")
                    }
                }
            }
        }

//        处理主从字段和关联字段的级联删除动作
        for (fieldInfo in refFieldList) {
            if (fieldInfo.refDeletePolicy == "cascade") {
                if (fieldInfo.type == "master_slave" || fieldInfo.type == "reference") {
                    val refObjectInfo = requireNotNull(getObjectById(fieldInfo.objectId))
                    var subDataList = query(
                        objectApiName = refObjectInfo.api,
                        expr = "\"${fieldInfo.api}\" = ?",
                        params = listOf(masterDataId),
                        offset = 0,
                        limit = 500,
                        fieldSet = setOf("id")
                    )
                    while (subDataList.isNotEmpty()) {
                        for (subData in subDataList) {
                            val id = requireNotNull(subData["id"]) as String
                            if (withTrigger) {
                                delete(refObjectInfo.api, id)
                            } else {
                                deleteWithoutTrigger(refObjectInfo.api, id)
                            }
                        }
                        subDataList = query(
                            objectApiName = refObjectInfo.api,
                            expr = "\"${fieldInfo.api}\" = ?",
                            params = listOf(masterDataId),
                            offset = 0,
                            limit = 500,
                            fieldSet = setOf("id")
                        )
                    }
                }
            }
        }

//        处理主从字段和关联字段的 set_null 动作
        for (fieldInfo in refFieldList) {
            if (fieldInfo.refDeletePolicy == "set_null") {
                if (fieldInfo.type == "master_slave" || fieldInfo.type == "reference") {
                    val refObjectInfo = requireNotNull(getObjectById(fieldInfo.objectId))
                    //language=PostgreSQL
                    sql(
                        """
                            update hymn_view."${refObjectInfo.api}"
                            set "${fieldInfo.api}" = null
                            where "${fieldInfo.api}" = ?
                        """, masterDataId
                    )
                }
            }
        }
    }
}