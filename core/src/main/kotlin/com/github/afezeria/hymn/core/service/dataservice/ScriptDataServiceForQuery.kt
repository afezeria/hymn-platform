package com.github.afezeria.hymn.core.service.dataservice

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.exception.PermissionDeniedException
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.common.platform.dataservice.FieldInfo
import com.github.afezeria.hymn.common.util.execute
import com.github.afezeria.hymn.common.util.toJson
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

    override fun count(
        objectApiName: String,
        expr: String,
        params: Collection<Any>
    ): Long {
        getObjectByApi(objectApiName) ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        val whereExpression = if (expr.isNotBlank()) {
            " where " + CCJSqlParserUtil.parseCondExpression(expr).apply {
                accept(qualifyTableForColumns)
            }.toString()
        } else {
            ""
        }
        val sql = """
            select count(*) count from hymn_view."$objectApiName" main $whereExpression
        """
        val resultMap = database.useConnection {
            it.execute(sql, params).firstOrNull()
        }
        return requireNotNull(resultMap?.get("count")) as Long
    }


    override fun query(
        objectApiName: String,
        expr: String,
        params: Collection<Any>
    ): MutableList<MutableMap<String, Any?>> {
        return query(objectApiName, expr, params, 0, 500, emptySet())
    }

    override fun query(
        objectApiName: String,
        expr: String,
        params: Collection<Any>,
        offset: Long,
        limit: Long
    ): MutableList<MutableMap<String, Any?>> {
        return query(objectApiName, expr, params, offset, limit, emptySet())
    }

    override fun queryById(objectApiName: String, id: String): MutableMap<String, Any?>? {
        return query(objectApiName, "id = ?", listOf(id), 0, 1).firstOrNull()
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
        offset: Long,
        limit: Long,
        fieldSet: Set<String>,
    ): MutableList<MutableMap<String, Any?>> {
        getObjectByApi(objectApiName) ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        val fieldApiMap = getFieldApiMap(objectApiName)
        val summaryFields = mutableListOf<FieldInfo>()

        val columns = StringBuilder("id").apply {
            fieldApiMap.asIterable().run {
                if (fieldSet.isEmpty()) this
                else filter { fieldSet.contains(it.key) }
            }.forEach {
                if (it.value.type == "summary") {
                    summaryFields.add(it.value)
                } else {
                    append(",main.\"")
                    append(it.value.api)
                    append("\"")
                }
            }
        }.toString()

        val whereExpression = if (expr.isNotBlank()) {
            " where " + CCJSqlParserUtil.parseCondExpression(expr).apply {
                accept(qualifyTableForColumns)
            }.toString()
        } else {
            ""
        }

        val limitAndOffset = "limit $limit offset $offset"

        val sql = """
            select $columns from hymn_view."$objectApiName" main $whereExpression
            $limitAndOffset
        """
        val resultIdMap = mutableMapOf<String, MutableMap<String, Any?>>()
        database.useConnection {
            for (data in it.execute(sql, params)) {
                resultIdMap[requireNotNull(data["id"]) as String] = data
            }
        }
//        查询汇总数据
        processSummaryField(resultIdMap, summaryFields)
//        查询关联数据
        processRefField(resultIdMap, fieldApiMap.values)

        return resultIdMap.mapTo(ArrayList()) { it.value }
    }


    override fun queryWithPerm(
        objectApiName: String,
        expr: String,
        params: Collection<Any>,
    ): MutableList<MutableMap<String, Any?>> {
        return queryWithPerm(objectApiName, expr, emptyList(), 0, 500, emptySet())
    }

    override fun queryWithPerm(
        objectApiName: String,
        expr: String,
        params: Collection<Any>,
        offset: Long,
        limit: Long,
    ): MutableList<MutableMap<String, Any?>> {
        return queryWithPerm(objectApiName, expr, emptyList(), offset, limit, emptySet())
    }

    override fun queryByIdWithPerm(
        objectApiName: String,
        id: String
    ): MutableMap<String, Any?>? {
        return queryWithPerm(objectApiName, "id = ?", listOf(id), 0, 1).firstOrNull()
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
        offset: Long,
        limit: Long,
        fieldSet: Set<String>,
    ): MutableList<MutableMap<String, Any?>> {
        if (offset < 0) throw BusinessException("offset must be greater than or equal to 0")
        if (limit in 1..500) throw BusinessException("limit must be between 1 and 500")

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

        val visibleTypeIdSet = getVisibleTypeIdSet(roleId, objectApiName)
        val typeWhereStr = if (getTypeList(objectApiName).size == visibleTypeIdSet.size) {
            ""
        } else {
            " and type_id = any (?) "
        }


//        获取有读权限的字段集合
        val fieldApiMap = getFieldApiMap(objectApiName)
        val fieldList = mutableListOf<FieldInfo>()
        val readAbleFieldApiSet = getFieldApiSetWithPerm(roleId, objectApiName, read = true)
        val summaryFields = mutableListOf<FieldInfo>()

//        过滤并分离普通字段和汇总字段
        if (fieldSet.isNotEmpty()) {
            for (value in fieldApiMap.values) {
                if (fieldSet.contains(value.api) && readAbleFieldApiSet.contains(value.api)) {
                    if (value.type == "summary") {
                        summaryFields.add(value)
                    } else {
                        fieldList.add(value)
                    }
                }
            }
        } else {
            for (value in fieldApiMap.values) {
                if (readAbleFieldApiSet.contains(value.api)) {
                    if (value.type == "summary") {
                        summaryFields.add(value)
                    } else {
                        fieldList.add(value)
                    }
                }
            }
        }

        val columns = StringBuilder("id").apply {
            for (field in fieldList) {
                append(",main.\"")
                append(field.api)
                append("\"")
            }
        }.toString()


        val limitAndOffset = "limit $limit offset $offset"

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
        val resultIdMap = mutableMapOf<String, MutableMap<String, Any?>>()
        database.useConnection {
            for (data in it.execute(sql, parameterList)) {
                resultIdMap[requireNotNull(data["id"]) as String] = data
            }
        }
//        查询汇总数据
        processSummaryField(resultIdMap, summaryFields)
//        查询关联数据
        processRefField(resultIdMap, fieldList)

        return resultIdMap.mapTo(ArrayList()) { it.value }
    }

    /**
     * 将汇总字段的值插入查询结果中
     */
    private fun processSummaryField(
        result: MutableMap<String, MutableMap<String, Any?>>,
        summaryFields: Collection<FieldInfo>
    ) {
        if (result.isNotEmpty()) {
            for (field in summaryFields) {
                val summaryResult = getSummaryValue(field, result.keys)
                for (map in summaryResult) {
                    val id = requireNotNull(map["_master_id"])
                    val value = map["_summary"]
                    result[id]!![field.api] = value
                }
            }
        }
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

    private fun processRefField(
        result: MutableMap<String, MutableMap<String, Any?>>,
        fields: Collection<FieldInfo>
    ) {
        val objectId2DataIdSet = mutableMapOf<String, MutableSet<String>>()
        val refFields = fields.filter { it.refId != null }
        for (field in refFields) {
            val dataIdSet =
                objectId2DataIdSet.getOrPut(requireNotNull(field.refId)) { mutableSetOf() }
            if (field.type == "mreference") {
                for (data in result.values) {
                    val value = data[field.api] as String?
                    if (value != null) {
                        dataIdSet.addAll(value.split(","))
                    }
                }
            } else {
                for (data in result.values) {
                    val value = data[field.api] as String?
                    if (value != null) {
                        dataIdSet.add(value)
                    }
                }
            }
        }
        if (objectId2DataIdSet.isEmpty()) {
            return
        }
        val objectId2Id2Name = queryName(objectId2DataIdSet)
        for (field in refFields) {
            val id2Name = objectId2Id2Name[field.refId] ?: continue
            if (field.type == "mreference") {
                for (data in result.values) {
                    val value = data[field.api]?.run {
                        this as String
                        val builder = StringBuilder()
                        for (s in split(",")) {
                            val name = id2Name[s]
                            if (name != null) {
                                builder.append("${id2Name[s]}($this),")
                            }
                        }
                        if (builder.isEmpty()) {
                            null
                        } else {
                            builder.toString()
                        }
                    }
                    data[field.api + "_name"] = value
                }
            } else {
                for (data in result.values) {
                    val id = data[field.api] as String?
                    if (id != null) {
                        val name = id2Name[id]
                        if (name != null) {
                            data[field.api + "_name"] = name
                            continue
                        }
                    }
                    data[field.api + "_name"] = null
                }
            }
        }
    }


    /**
     * 用于 query 系列方法根据字段关联的对象id和数据id查询的对应的数据的name字段值
     * 不限制权限
     */
    private fun queryName(objectId2DataIdSet: Map<String, Set<String>>): Map<String, Map<String, String>> {
        database.useConnection {
            val result = mutableMapOf<String, MutableMap<String, String>>()
            it.execute(
                "select * from hymn.query_data_name(cast(? as json)) t(object_id text,data_id text,data_name text)",
                objectId2DataIdSet.toJson()
            ) {
                requireNotNull(it)
                it.apply {
                    while (next()) {
                        result.getOrPut(getString(1)) { mutableMapOf() }
                            .also {
                                it[getString(2)] = getString(3)
                            }
                    }
                }
            }
            return result
        }
    }

}