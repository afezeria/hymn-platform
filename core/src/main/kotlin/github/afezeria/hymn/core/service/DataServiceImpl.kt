package github.afezeria.hymn.core.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.exception.PermissionDeniedException
import github.afezeria.hymn.common.platform.DataService
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.core.module.entity.BizObjectField
import github.afezeria.hymn.core.module.service.*
import mu.KLogging
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.select.SubSelect
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

/**
 * @author afezeria
 */
class DataServiceImpl : DataService {
    companion object : KLogging() {
        val qualifyTableForColumns = object : ExpressionVisitorAdapter() {
            override fun visit(subSelect: SubSelect?) {
                throw InnerException("无效的where表达式，表达式中不能包含子查询")
            }

            override fun visit(column: Column?) {
                column?.apply {
                    table = Table("main")
                }
                super.visit(column)
            }
        }
    }

    @Autowired
    private lateinit var bizObjectService: BizObjectService

    @Autowired
    private lateinit var typeService: BizObjectTypeService

    @Autowired
    private lateinit var fieldService: BizObjectFieldService

    @Autowired
    private lateinit var objectPermService: BizObjectPermService

    @Autowired
    private lateinit var typePermService: BizObjectTypePermService

    @Autowired
    private lateinit var fieldPermService: BizObjectFieldPermService

    @Autowired
    private lateinit var databaseService: DatabaseService

    override fun query(
        objectApiName: String, expr: String,
        offset: Long?,
        limit: Long?,
    ): MutableList<MutableMap<String, Any?>> {
        return query(objectApiName, expr, emptyList(), offset, limit)
    }


    override fun query(
        objectApiName: String,
        expr: String,
        params: List<Any>,
        offset: Long?,
        limit: Long?,
        fieldSet: Set<String>,
    ): MutableList<MutableMap<String, Any?>> {
        val bizObject = bizObjectService.findByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        if (!bizObject.active) {
            throw InnerException("对象 [api:$objectApiName] 已停用")
        }
        val limitAndOffset =
            "${if (limit != null) "limit $limit" else ""} ${if (offset != null) "offset $offset" else ""}"
        val sql = """
            select * from hymn_view."$objectApiName" where $expr
            $limitAndOffset
        """
        databaseService.db().useConnection {
            return it.execute(sql, params)
        }
    }

    override fun query(
        objectApiName: String,
        condition: Map<String, Any?>,
        offset: Long?,
        limit: Long?,
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
        return query(objectApiName, expr, params, offset, limit)
    }

    override fun queryById(objectApiName: String, id: String): MutableMap<String, Any?>? {
        return query(objectApiName, "id = ?", listOf(id)).firstOrNull()
    }

    override fun queryWithPerm(
        objectApiName: String,
        expr: String,
        offset: Long?,
        limit: Long?,
    ): MutableList<MutableMap<String, Any?>> {
        return queryWithPerm(objectApiName, expr, emptyList(), offset, limit)
    }

    override fun queryWithPerm(
        objectApiName: String,
        expr: String,
        params: List<Any>,
        offset: Long?,
        limit: Long?,
        fieldSet: Set<String>,
    ): MutableList<MutableMap<String, Any?>> {
//        查询对象及权限
        val bizObject = bizObjectService.findByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName] 不存在")
        if (!bizObject.active) {
            throw InnerException("对象 [api:$objectApiName] 已停用")
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
                ?: throw PermissionDeniedException("缺少对象 [api:$objectApiName] 查看权限")
        if (!objectPerm.que) {
            throw PermissionDeniedException("缺少对象 [api:$objectApiName] 查看权限")
        }

        val typeList = typeService.findByBizObjectId(objectId)
        val typePermList =
            typePermService.findByRoleIdAndBizObjectId(roleId, objectId)
//        TODO()
//        val typeCondStr = if (typeList.size == typePermList.size) {
//
//
//        } else {
//            ""
//        }


//        获取有读权限的字段集合
        val fieldList = fieldService.findByBizObjectId(objectId)
        if (fieldSet.isNotEmpty()) {
            fieldList.removeIf { !fieldSet.contains(it.api) }
        }
        val fieldPermList =
            fieldPermService.findByRoleIdAndFieldIdList(roleId, fieldList.map { it.id })
        val canReadFieldIdSet = mutableSetOf<String>()
        for (perm in fieldPermList) {
            if (perm.pRead) canReadFieldIdSet.add(perm.fieldId)
        }


        val columns =
            "id, " + fieldList.asSequence().filter { canReadFieldIdSet.contains(it.id) }
                .map { "main.\"${it.api}\"" }
                .joinToString(", ")

        val limitAndOffset =
            "${if (limit != null) "limit $limit" else ""} ${if (offset != null) "offset $offset" else ""}"

        val whereExpression = if (expr.isNotBlank()) {
            " and (" + CCJSqlParserUtil.parseCondExpression(expr).apply {
                accept(qualifyTableForColumns)
            }.toString() + ") "
        } else {
            ""
        }

        //language=PostgreSQL
        val shareTableJoinStr = if (sharedTableName != null) """
                         left join (select data_id
                                    from hymn_view.$sharedTableName
                                    where (account_id = ?)
                                       or (role_id = ?)
                                       or (org_id = ?)) shared(_data_id) on shared._data_id = main.id
                         """ else ""
        val shareTableWhereStr =
            if (sharedTableName != null) " or shared._data_id is not null " else ""

        val parameterList = arrayListOf<Any>()
        var sql: String
        if (objectPerm.queryAll) {
//            查询全部数据
            //language=PostgreSQL
            sql = "select $columns from hymn_view.\"${bizObject.api}\" where $expr"
        } else if (objectPerm.queryWithAccountTree) {
//            查询本人及直接下属的数据
            //language=PostgreSQL
            sql = """
                select main.*
                from hymn_view.account main
                         left join (select id
                                    from hymn_view.account
                                    where leader_id = ?) subordinates(_account_id) on main.owner = subordinates._account_id
                         $shareTableJoinStr
                where (
                        subordinates._account_id is not null or main.owner = ?
                        $shareTableWhereStr
                    )
                  $whereExpression
                $limitAndOffset
            """
            parameterList.add(session.accountId)
            if (sharedTableName != null) {
                parameterList.add(session.accountId)
                parameterList.add(session.roleId)
                parameterList.add(session.orgId)
            }
            parameterList.add(session.accountId)
            parameterList.addAll(params)
        } else if (objectPerm.queryWithOrg) {
//            查询本部门数据
            //language=PostgreSQL
            sql = """
                select main.*
                from hymn_view.account main
                         left join (select id
                                    from hymn_view.account
                                    where org_id = ?) org(_account_id) on main.owner = org._account_id
                         $shareTableJoinStr
                where (
                        org._account_id is not null or main.owner = ?
                        $shareTableWhereStr
                    )
                  $whereExpression
                $limitAndOffset
            """
            parameterList.add(session.orgId)
            if (sharedTableName != null) {
                parameterList.add(session.accountId)
                parameterList.add(session.roleId)
                parameterList.add(session.orgId)
            }
            parameterList.add(session.accountId)
            parameterList.addAll(params)
        } else if (objectPerm.queryWithOrgTree) {
//            查询本部门及下级部门数据
//            查询下级部门时最大深度为15，当前部门深度为1,深度超过15的部门的数据不显示
            //language=PostgreSQL
            sql = """
                with recursive
                    org_tree(id, parent_id, depth) as (
                        select id, parent_id, 1
                        from hymn_view.org
                        where id = ?
                        union
                        select a.id, a.parent_id, depth + 1
                        from hymn_view.org a
                                 inner join org_tree on org_tree.id = a.parent_id
                        where depth < 15
                    ),
                    orgs(_account_id) as (
                        select account.id
                        from hymn_view.account
                                 inner join org_tree on account.org_id = org_tree.id
                    )
                select main.*
                from hymn_view.account main
                         left join orgs on main.owner = orgs._account_id
                         $shareTableJoinStr
                where (
                        orgs._account_id is not null or main.owner = ?
                        $shareTableWhereStr
                    )
                  $whereExpression
                $limitAndOffset
            """
            parameterList.add(session.orgId)
            if (sharedTableName != null) {
                parameterList.add(session.accountId)
                parameterList.add(session.roleId)
                parameterList.add(session.orgId)
            }
            parameterList.add(session.accountId)
            parameterList.addAll(params)
        } else {
//            查询自己的数据
            //language=PostgreSQL
            sql = """
                select main.*
                from hymn_view.account main
                         $shareTableJoinStr
                where (
                        main.owner = ?
                        $shareTableWhereStr
                    )
                  $whereExpression
                $limitAndOffset
            """
            if (sharedTableName != null) {
                parameterList.add(session.accountId)
                parameterList.add(session.roleId)
                parameterList.add(session.orgId)
            }
            parameterList.add(session.accountId)
            parameterList.addAll(params)

        }
        databaseService.user().useConnection {
            return it.execute(sql, params)
        }
    }

    override fun queryWithPerm(
        objectApiName: String,
        condition: Map<String, Any?>,
        offset: Long?,
        limit: Long?,
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
        return queryWithPerm(objectApiName, expr, params, offset, limit)
    }

    override fun queryByIdWithPerm(
        objectApiName: String,
        id: String
    ): MutableMap<String, Any?>? {
        return queryWithPerm(objectApiName, "id = ?", listOf(id)).firstOrNull()
    }

    override fun insert(
        objectApiName: String,
        data: MutableMap<String, Any?>,
        trigger: Boolean
    ): String {
//        查询对象及权限
        val bizObject = bizObjectService.findByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        if (!bizObject.active) {
            throw InnerException("对象 [api:$objectApiName] 已停用")
        }
        val bizObjectId = bizObject.id
        val session = Session.getInstance()
        val roleId = session.roleId

        val objectPerm =
            objectPermService.findByRoleIdAndBizObjectId(roleId, bizObjectId)
                ?: throw PermissionDeniedException("缺少对象 [api:$objectApiName] 新建权限")
        if (!objectPerm.ins) {
            throw PermissionDeniedException("缺少对象 [api:$objectApiName] 新建权限")
        }


//        查询字段及权限
        val id2FieldMap = mutableMapOf<String, BizObjectField>()
        for (field in fieldService.findByBizObjectId(bizObject.id)) {
            if (data.containsKey(field.api)) {
                id2FieldMap[field.id] = field
            }
        }
        val fieldPermList =
            fieldPermService.findByRoleIdAndFieldIdList(session.roleId, id2FieldMap.keys.toList())
        val insertData = mutableMapOf<String, Any?>()
        for (perm in fieldPermList) {
            if (perm.pEdit) {
                val api = id2FieldMap[perm.fieldId]!!.api
                insertData[api] = data[api]
            }
        }

        val accountId = session.accountId
        val now = LocalDateTime.now()
        insertData.putIfAbsent("owner_id", accountId)
        insertData.putIfAbsent("create_by_id", accountId)
        insertData.putIfAbsent("modify_by_id", accountId)
        insertData.putIfAbsent("create_date", now)
        insertData.putIfAbsent("modify_date", now)
//        insertData.putIfAbsent("type_id")
        val columns = insertData.keys.joinToString(",")
        val params = "?" + ",?".repeat(insertData.keys.size - 1)
        //language=PostgreSQL
        val sql = """
            insert into hymn_view.$objectApiName ($columns) values ();
        """



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