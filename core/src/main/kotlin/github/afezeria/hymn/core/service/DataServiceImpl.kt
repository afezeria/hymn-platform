package github.afezeria.hymn.core.service

import github.afezeria.hymn.common.platform.DataService
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.core.module.service.BizObjectFieldPermService
import github.afezeria.hymn.core.module.service.BizObjectFieldService
import github.afezeria.hymn.core.module.service.BizObjectPermService
import github.afezeria.hymn.core.module.service.BizObjectService
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
class DataServiceImpl : DataService {
    companion object : KLogging()

    @Autowired
    private lateinit var bizObjectService: BizObjectService

    @Autowired
    private lateinit var fieldService: BizObjectFieldService

    @Autowired
    private lateinit var objectPermService: BizObjectPermService

    @Autowired
    private lateinit var fieldPermService: BizObjectFieldPermService

    @Autowired
    private lateinit var databaseService: DatabaseService

    override fun query(objectApiName: String, expr: String): MutableList<MutableMap<String, Any?>> {
        return query(objectApiName, expr, emptyList())
    }


    override fun query(
        objectApiName: String,
        expr: String,
        params: List<Any>
    ): MutableList<MutableMap<String, Any?>> {
        val bizObject = bizObjectService.findByApi(objectApiName)
        if (bizObject == null) {
            logger.info("object [api:{}] not exist", objectApiName)
            return arrayListOf()
        }
        val sql = """
            select * from hymn_view."$objectApiName" where $expr
        """.trimIndent()
        databaseService.db().useConnection {
            return it.execute(sql, params)
        }
    }

    override fun query(
        objectApiName: String,
        condition: Map<String, Any?>
    ): MutableList<MutableMap<String, Any?>> {
        val params = mutableListOf<Any>()
        val expr = condition.map { (k, v) ->
            if (v == null) {
                "(\"$k\" is null)"
            } else {
                params.add(v)
                "(\"$k\" = ?)"
            }
        }.joinToString(" and ")
        return query(objectApiName, expr, params)
    }

    override fun queryById(objectApiName: String, id: String): MutableMap<String, Any?>? {
        return query(objectApiName, "id = ?", listOf(id)).firstOrNull()
    }

    override fun queryWithPerm(
        objectApiName: String,
        expr: String
    ): MutableList<MutableMap<String, Any?>> {
        return queryWithPerm(objectApiName, expr, emptyList())
    }

    override fun queryWithPerm(
        objectApiName: String,
        expr: String,
        params: List<Any>,
        fieldSet: Set<String>,
    ): MutableList<MutableMap<String, Any?>> {
        val bizObject = bizObjectService.findByApi(objectApiName)
        if (bizObject == null) {
            logger.info("object [api:{}] not exist", objectApiName)
            return arrayListOf()
        }
        val sharedTableName = if (bizObject.type == "custom") {
            bizObject.api + "_share"
        } else {
            null
        }
        val session = Session.getInstance()
        val roleId = session.roleId
        val objectId = bizObject.id
        val objectPerm =
            objectPermService.findByRoleIdAndBizObjectId(roleId, bizObject.id)
        if (objectPerm == null) {
            logger.info("no read permission for $objectApiName object")
            return arrayListOf()
        }
//        拼接字段列表
        val fieldList = fieldService.findByBizObjectId(objectId)
        if (fieldSet.isNotEmpty()) {
            fieldList.removeIf { !fieldSet.contains(it.api) }
        }
        val fieldIdList = fieldList.map { it.id }
        val canReadFieldIdSet =
            fieldPermService.findByRoleIdAndFieldIdList(roleId, fieldIdList)
                .asSequence().filter { it.pRead }.map { it.id }.toSet()
        val columns = fieldList.asSequence().filter { canReadFieldIdSet.contains(it.id) }
            .map { "main.\"${it.api}\"" }
            .joinToString(", ")
        val with = """
            
        """.trimIndent()
        val sql = "select $columns from hymn_view.\"${bizObject.api}\" as main where $expr"
        databaseService.db().useConnection {
            return it.execute(sql, params)
        }
        TODO()
    }

    override fun queryWithPerm(
        objectApiName: String,
        condition: Map<String, Any?>
    ): MutableList<MutableMap<String, Any?>> {
        val params = mutableListOf<Any>()
        val expr = condition.map { (k, v) ->
            if (v == null) {
                "(\"$k\" is null)"
            } else {
                params.add(v)
                "(\"$k\" = ?)"
            }
        }.joinToString(" and ")
        return queryWithPerm(objectApiName, expr, params)
    }

    override fun queryByIdWithPerm(
        objectApiName: String,
        id: String
    ): MutableMap<String, Any?>? {
        return queryWithPerm(objectApiName, "id = ?", listOf(id)).firstOrNull()
    }

    override fun insert(objectApiName: String, data: MutableMap<String, Any?>): String {
        TODO("Not yet implemented")
    }

    override fun insert(
        objectApiName: String,
        data: MutableMap<String, Any?>,
        trigger: Boolean
    ): String {
        TODO("Not yet implemented")
    }

    override fun insertWithPerm(objectApiName: String, data: MutableMap<String, Any?>): String {
        TODO("Not yet implemented")
    }

    override fun batchInsert(
        objectApiName: String,
        dataList: MutableList<MutableMap<String, Any?>>
    ): List<String> {
        TODO("Not yet implemented")
    }

    override fun batchInsert(
        objectApiName: String,
        dataList: MutableList<MutableMap<String, Any?>>,
        trigger: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun batchInsertWithPerm(
        objectApiName: String,
        dataList: MutableList<MutableMap<String, Any?>>
    ) {
        TODO("Not yet implemented")
    }

    override fun update(
        objectApiName: String,
        data: MutableMap<String, Any?>
    ): MutableMap<String, Any?> {
        TODO("Not yet implemented")
    }

    override fun update(
        objectApiName: String,
        data: MutableMap<String, Any?>,
        trigger: Boolean
    ): MutableMap<String, Any?> {
        TODO("Not yet implemented")
    }

    override fun update(
        objectApiName: String,
        id: String,
        data: MutableMap<String, Any?>
    ): MutableMap<String, Any?> {
        TODO("Not yet implemented")
    }

    override fun update(
        objectApiName: String,
        id: String,
        data: MutableMap<String, Any?>,
        trigger: Boolean
    ): MutableMap<String, Any?> {
        TODO("Not yet implemented")
    }

    override fun updateWithPerm(
        objectApiName: String,
        data: MutableMap<String, Any?>
    ): MutableMap<String, Any?> {
        TODO("Not yet implemented")
    }

    override fun updateWithPerm(
        objectApiName: String,
        id: String,
        data: MutableMap<String, Any?>
    ): MutableMap<String, Any?> {
        TODO("Not yet implemented")
    }

    override fun batchUpdate(
        objectApiName: String,
        data: MutableMap<String, Any?>
    ): MutableList<MutableMap<String, Any?>> {
        TODO("Not yet implemented")
    }

    override fun batchUpdate(
        objectApiName: String,
        data: MutableMap<String, Any?>,
        trigger: Boolean
    ): MutableList<MutableMap<String, Any?>> {
        TODO("Not yet implemented")
    }

    override fun batchUpdateWithPerm(
        objectApiName: String,
        data: MutableMap<String, Any?>
    ): MutableList<MutableMap<String, Any?>> {
        TODO("Not yet implemented")
    }

    override fun delete(objectApiName: String, id: String): MutableMap<String, Any?>? {
        TODO("Not yet implemented")
    }

    override fun delete(
        objectApiName: String,
        id: String,
        trigger: Boolean
    ): MutableMap<String, Any?>? {
        TODO("Not yet implemented")
    }

    override fun deleteWithPerm(objectApiName: String, id: String): MutableMap<String, Any?>? {
        TODO("Not yet implemented")
    }

    override fun batchDelete(
        objectApiName: String,
        ids: MutableList<String>
    ): Pair<MutableList<String>, MutableList<MutableMap<String, Any?>>> {
        TODO("Not yet implemented")
    }

    override fun batchDelete(
        objectApiName: String,
        ids: MutableList<String>,
        trigger: Boolean
    ): MutableList<MutableMap<String, Any?>> {
        TODO("Not yet implemented")
    }

    override fun batchDeleteWithPerm(
        objectApiName: String,
        ids: MutableList<String>
    ): Pair<MutableList<String>, MutableList<MutableMap<String, Any?>>> {
        TODO("Not yet implemented")
    }

    override fun sql(sql: String): MutableList<MutableMap<String, Any?>> {
        TODO("Not yet implemented")
    }

    override fun sql(sql: String, params: Array<Any>): MutableList<MutableMap<String, Any?>> {
        TODO("Not yet implemented")
    }

    override fun sql(sql: String, params: Map<String, Any>): MutableList<MutableMap<String, Any?>> {
        TODO("Not yet implemented")
    }

    override fun hasDataPerm(
        accountId: String,
        objectId: String,
        dataId: String,
        read: Boolean?,
        update: Boolean?,
        share: Boolean?,
        owner: Boolean?
    ): Boolean {
        TODO("Not yet implemented")
    }

}