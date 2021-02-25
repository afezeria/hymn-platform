package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.exception.PermissionDeniedException
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.platform.dataservice.FieldInfo
import github.afezeria.hymn.common.util.execute

/**
 * @author afezeria
 */
interface ScriptDataServiceForDelete : ScriptDataServiceForQuery {

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
        getObjectByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        database.useConnection {
            return it.execute(
                """
                delete from hymn_view."$objectApiName" where id = any (?)
                returning *
            """, ids
            )
        }
    }


    private fun batchDelete(
        objectApiName: String,
        ids: List<String>,
        withPerm: Boolean,
    ): MutableList<MutableMap<String, Any?>> {
        if (ids.isEmpty()) throw BusinessException("待删除数据id列表为空不能为空")
        if (ids.size > 100) throw BusinessException("批量删除数据一次不能大于100条")

        getObjectByApi(objectApiName) ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        var oldDataList: MutableList<MutableMap<String, Any?>>

        val fieldInfoList = getFieldApiMap(objectApiName)
            .mapNotNullTo(mutableListOf()) { if (it.value.type != "summary") it.value else null }
        if (withPerm) {
            val session = Session.getInstance()
            val objectPerm = getObjectPerm(session.roleId, objectApiName)
                ?: throw PermissionDeniedException("缺少对象 [api:$objectApiName] 删除权限")
            if (!objectPerm.del) {
                throw PermissionDeniedException("缺少对象 [api:$objectApiName] 删除权限")
            }
            oldDataList = queryWithPerm(
                objectApiName, "id = any (?)", ids,
                fieldSet = emptySet(),
            )
            if (!objectPerm.editAll) {
//                如果没有对象的编辑全部权限则过滤掉所有人不是当前用户的数据
                oldDataList = oldDataList.filterTo(ArrayList()) {
                    it["owner_id"] == session.accountId
                }
            }
            val readableFieldApiSet =
                getFieldApiSetWithPerm(Session.getInstance().roleId, objectApiName, read = true)
            fieldInfoList.removeIf { !readableFieldApiSet.contains(it.api) }
        } else {
            oldDataList = queryByIds(objectApiName, ids)
        }
        if (oldDataList.isEmpty()) return mutableListOf()

        return oldDataList.mapTo(ArrayList()) {
            deleteHelper(
                objectApiName,
                it,
                fieldInfoList
            )
        }
    }

    private fun deleteHelper(
        objectApiName: String,
        old: MutableMap<String, Any?>,
        fields: Collection<FieldInfo>,
    ): MutableMap<String, Any?> {
        val returnColumns =
            "\"id\"" + fields.joinToString("\",\"", ",\"", "\"") { it.api }

        //language=PostgreSQL
        val sql =
            """
                delete from hymn_view."$objectApiName" where id = ? 
                returning $returnColumns
            """
        return execute(sql, listOf(old["id"]), WriteType.DELETE, objectApiName, old, null, true)
    }
}