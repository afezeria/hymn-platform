package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.util.execute

/**
 * @author afezeria
 */
interface ScriptDataServiceForDelete : ScriptDataServiceForQuery {

    override fun delete(
        objectApiName: String,
        id: String,
    ): MutableMap<String, Any?> {
        return batchDelete(objectApiName, listOf(id), false).first()
    }

    override fun batchDelete(
        objectApiName: String,
        ids: MutableList<String>
    ): MutableList<MutableMap<String, Any?>> {
        return batchDelete(objectApiName, ids, false)
    }

    override fun deleteWithPerm(objectApiName: String, id: String): MutableMap<String, Any?> {
        return batchDelete(objectApiName, listOf(id), true).first()
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
        getObject(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        database.useConnection {
            return it.execute(
                """
                delete from hymn_view."$objectApiName" where id = any (?)
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

        getObject(objectApiName) ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        val oldDataList: MutableList<MutableMap<String, Any?>>
        var readableFieldApiSet: Set<String>? = null
        if (withPerm) {
            oldDataList = queryWithPerm(
                objectApiName, "id = any (?)", ids,
                fieldSet = emptySet(),
                writeable = true,
            )
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
        fields: Collection<String>? = null,
    ): MutableMap<String, Any?> {
        val returnColumns = if (fields == null) {
            old.keys.joinToString("\",\"", ",\"", "\"")
        } else {
            "\"id\"" + fields.joinToString("\",\"", ",\"", "\"")
        }

        //language=PostgreSQL
        val sql =
            """
                delete from hymn_view."$objectApiName" where id = ? 
                returning $returnColumns
            """
        return execute(sql, listOf(old["id"]), WriteType.DELETE, old, null, true)
    }
}