package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.exception.PermissionDeniedException
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.platform.dataservice.FieldInfo
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.toJson
import mu.KLogging
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.select.SubSelect

/**
 * @author afezeria
 */
interface ScriptDataServiceForQuery : ScriptDataService {
    companion object : KLogging() {
        val qualifyTableForColumns = object : ExpressionVisitorAdapter() {
            override fun visit(subSelect: SubSelect?) {
                throw BusinessException("无效的where表达式，表达式中不能包含子查询")
            }

            override fun visit(column: Column?) {
                column?.apply {
                    table = Table("main")
                }
                super.visit(column)
            }
        }
    }

    override fun query(
        objectApiName: String, expr: String,
        offset: Long?,
        limit: Long?,
    ): MutableList<MutableMap<String, Any?>> {
        return query(objectApiName, expr, emptyList(), offset, limit)
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

    override fun queryByIds(
        objectApiName: String,
        ids: Collection<String>
    ): MutableList<MutableMap<String, Any?>> {
        return query(objectApiName, "id = any (?)", listOf(ids))
    }

    override fun query(
        objectApiName: String,
        expr: String,
        params: Collection<Any>,
        offset: Long?,
        limit: Long?,
        fieldSet: Set<String>,
    ): MutableList<MutableMap<String, Any?>> {
        getObjectByApi(objectApiName) ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        val columns = if (fieldSet.isEmpty()) {
            getFieldApiMap(objectApiName).keys.joinToString(", ")
        } else {
            getFieldApiMap(objectApiName).keys.filter { fieldSet.contains(it) }.joinToString(", ")
        }

        val limitAndOffset =
            "${if (limit != null) "limit $limit" else ""} ${if (offset != null) "offset $offset" else ""}"
        val sql = """
            select $columns from hymn_view."$objectApiName" where $expr
            $limitAndOffset
        """
        database.useConnection {
            return it.execute(sql, params)
        }
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

    override fun queryByIdsWithPerm(
        objectApiName: String,
        ids: Collection<String>
    ): MutableList<MutableMap<String, Any?>> {
        return queryWithPerm(objectApiName, "id = any (?)", listOf(ids))
    }

    override fun queryWithPerm(
        objectApiName: String,
        expr: String,
        params: Collection<Any>,
        offset: Long?,
        limit: Long?,
        fieldSet: Set<String>,
    ): MutableList<MutableMap<String, Any?>> {
//        查询对象及权限
        val bizObject = getObjectByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        val sharedTableName = if (bizObject.type == "custom") {
            bizObject.api + "_share"
        } else {
            null
        }
        val session = Session.getInstance()
        val roleId = session.roleId
        val objectPerm = getObjectPerm(roleId, objectApiName)
            ?: throw PermissionDeniedException("缺少对象 [api:$objectApiName] 查看权限")
        if (!objectPerm.que) {
            throw PermissionDeniedException("缺少对象 [api:$objectApiName] 查看权限")
        }
//        if (writeable && !objectPerm.ins) {
//            throw PermissionDeniedException("缺少对象 [api:$objectApiName] 更新权限")
//        }
//        if (deletable && !objectPerm.del) {
//            throw PermissionDeniedException("缺少对象 [api:$objectApiName] 删除权限")
//        }

        val visibleTypeIdSet = getVisibleTypeIdSet(roleId, objectApiName)
        val typeWhereStr = if (getTypeList(objectApiName).size == visibleTypeIdSet.size) {
            ""
        } else {
            " and type_id = any (?) "
        }


//        获取有读权限的字段集合
        val summaryFields =
            getFieldApiMap(objectApiName).mapNotNullTo(ArrayList()) { if (it.value.type == "summary") it.value else null }
        val readAbleFieldApiSet = getFieldApiSetWithPerm(roleId, objectApiName, read = true)
        val columnFields =
            if (fieldSet.isEmpty()) {
                readAbleFieldApiSet
            } else {
                readAbleFieldApiSet.filter { fieldSet.contains(it) }
            }.toMutableSet()
        for (summaryField in summaryFields) {
            columnFields.remove(summaryField.api)
        }

        val columns =
            "id, " + columnFields.joinToString(", ") { "main.\"$it\"" }

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
                         left join (select data_id,read_only
                                    from hymn_view.$sharedTableName
                                    where (account_id = ?)
                                       or (role_id = ?)
                                       or (org_id = ?)) shared(_data_id,_read_only) on shared._data_id = main.id
                         """ else ""
        val shareTableWhereStr =
            if (sharedTableName != null) " or shared._data_id is not null " else ""

        val parameterList = arrayListOf<Any>()
        var sql: String

        if (objectPerm.queryAll) {
//            查询全部数据
            //language=PostgreSQL
            sql = """
                    select $columns from hymn_view."${bizObject.api}" main
                    where (true $typeWhereStr) $whereExpression
                """
            if (typeWhereStr.isNotEmpty()) {
                parameterList.add(visibleTypeIdSet)
            }
            parameterList.addAll(params)
        } else if (objectPerm.queryWithAccountTree) {
//            查询本人及直接下属的数据
            //language=PostgreSQL
            sql = """
                select $columns
                from hymn_view."${bizObject.api}" main
                         left join (select id
                                    from hymn_view.account
                                    where leader_id = ?) subordinates(_account_id) on main.owner_id = subordinates._account_id
                         $shareTableJoinStr
                where (
                        subordinates._account_id is not null or main.owner_id = ?
                        $shareTableWhereStr $typeWhereStr
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
            if (typeWhereStr.isNotEmpty()) {
                parameterList.add(visibleTypeIdSet)
            }
            parameterList.add(session.accountId)
            parameterList.addAll(params)
        } else if (objectPerm.queryWithOrg) {
//            查询本部门数据
            //language=PostgreSQL
            sql = """
                select $columns
                from hymn_view."${bizObject.api}" main
                         left join (select id
                                    from hymn_view.account
                                    where org_id = ?) org(_account_id) on main.owner_id = org._account_id
                         $shareTableJoinStr
                where (
                        org._account_id is not null or main.owner_id = ?
                        $shareTableWhereStr $typeWhereStr
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
            if (typeWhereStr.isNotEmpty()) {
                parameterList.add(visibleTypeIdSet)
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
                select $columns
                from hymn_view."${bizObject.api}" main
                         left join orgs on main.owner_id = orgs._account_id
                         $shareTableJoinStr
                where (
                        orgs._account_id is not null or main.owner_id = ?
                        $shareTableWhereStr $typeWhereStr
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
            if (typeWhereStr.isNotEmpty()) {
                parameterList.add(visibleTypeIdSet)
            }
            parameterList.add(session.accountId)
            parameterList.addAll(params)
        } else {
//            查询自己的和其他用户共享的数据
            //language=PostgreSQL
            sql = """
                select $columns
                from hymn_view."${bizObject.api}" main
                         $shareTableJoinStr
                where (
                        main.owner_id = ?
                        $shareTableWhereStr $typeWhereStr
                    )
                  $whereExpression
                $limitAndOffset
            """
            if (sharedTableName != null) {
                parameterList.add(session.accountId)
                parameterList.add(session.roleId)
                parameterList.add(session.orgId)
            }
            if (typeWhereStr.isNotEmpty()) {
                parameterList.add(visibleTypeIdSet)
            }
            parameterList.add(session.accountId)
            parameterList.addAll(params)
        }
        sql = sql.trimIndent()
        val resultIdMap = mutableMapOf<String, MutableMap<String, Any?>>()
        database.useConnection {
            for (data in it.execute(sql, parameterList)) {
                resultIdMap[requireNotNull(data["id"]) as String] = data
            }
        }
//        查询汇总数据
        if (resultIdMap.isNotEmpty()) {
            for (field in summaryFields) {
                val summaryResult = getSummaryValue(field, resultIdMap.keys)
                for (map in summaryResult) {
                    val id = requireNotNull(map["_master_id"])
                    val value = map["_summary"]
                    resultIdMap[id]!![field.api] = value
                }
            }
        }
        return resultIdMap.mapTo(ArrayList()) { it.value }
    }

    private fun getSummaryValue(
        field: FieldInfo,
        ids: Collection<String>
    ): List<Map<String, String?>> {
        val subObject = getObjectById(requireNotNull(field.sId))
            ?: throw InnerException("汇总字段属性错误，汇总对象不存在，field:[id:${field.id}]")
        val subObjectFields = getFieldApiMap(subObject.api)
        val targetField =
            subObjectFields.values.find { it.id == requireNotNull(field.sFieldId) }
                ?: throw InnerException("汇总字段属性错误，汇总目标字段不存在，field:[id:${field.id}]")
        val masterFieldApi =
            subObjectFields.values.find { it.type == "master_slave" }?.api
                ?: throw InnerException("汇总字段属性错误，汇总对象缺少主从字段，field:[id:${field.id}]")

        val summrayColumn = when (field.sType) {
            "sum" -> "sum(\"${targetField.api}\")"
            "count" -> "count(*)"
            "min" -> "min(\"${targetField.api}\")"
            "max" -> "max(\"${targetField.api}\")"
            else -> throw InnerException("错误的汇总类型，field:[${field.toJson()}]")
        }
        //language=PostgreSQL
        val sql = """
            select "$masterFieldApi"::text as _master_id, $summrayColumn::text as _summary
            from hymn_view."${subObject.api}"
            where id = any (?)
            group by "$masterFieldApi"
        """.trimIndent()
        database.useConnection {
            return it.execute(sql, ids) as List<Map<String, String?>>
        }
    }
}