package com.github.afezeria.hymn.common.db

import com.github.afezeria.hymn.common.db.AutoFillType.*
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.common.util.Json
import com.github.afezeria.hymn.common.util.execute
import org.ktorm.dsl.*
import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.BinaryExpression
import org.ktorm.expression.ColumnAssignmentExpression
import org.ktorm.expression.OrderByExpression
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.support.postgresql.*
import java.time.LocalDateTime
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import org.ktorm.dsl.count as ktormDslCount

/**
 * @author afezeria
 */
abstract class AbstractDao<E : AbstractEntity, T : AbstractTable<E>>(
    val table: T,
    val databaseService: DatabaseService,
) {
    private val entityFields = table.getEntityFieldList().asSequence()

    companion object {
        val and: (ColumnDeclaring<Boolean>, ColumnDeclaring<Boolean>) -> BinaryExpression<Boolean> =
            ColumnDeclaring<Boolean>::and

        inline fun <E : Any> Query.mapToArray(table: BaseTable<E>): ArrayList<E> {
            return mapTo(ArrayList()) { table.createEntity(it) }
        }

        private val field =
            AssignmentsBuilder::class.memberProperties.find { it.name == "_assignments" }?.javaField!!.apply {
                isAccessible = true
            }

        private fun InsertOrUpdateOnConflictClauseBuilder.setExclude(column: Column<Any>) {
            val assignments = field.get(this) as ArrayList<ColumnAssignmentExpression<*>>
            assignments += ColumnAssignmentExpression(
                column.asExpression(),
                excluded(column).asExpression()
            )
        }
    }

    private val defaultOrder = if (table.containsField("createDate")) {
        listOf(table["create_date"].desc())
    } else {
        emptyList()
    }

    fun delete(condition: (T) -> ColumnDeclaring<Boolean>): Int {
        return databaseService.primary().delete(table, condition)
    }

    fun deleteById(id: String): Int {
        return databaseService.primary().delete(table) {
            it.id eq id
        }
    }

    fun deleteByIds(ids: List<String>): Int {
        return databaseService.primary().delete(table) {
            it.id inList ids
        }
    }

    fun update(e: E): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val selector = AutoFillSelector()
        for (entityField in entityFields.filter { it.autoFill?.fillOnUpdate ?: false }) {
            val current = selector.getCurrent(entityField.autoFill!!.type)
            entityField.field.set(e, current)
        }
        return databaseService.primary().update(table) {
            entityFields.filter { it.field.name != "id" && it.mutable }
                .forEach { field ->
                    set(field.column, field.field.get(e))
                }
            where {
                it.id eq e.id
            }
        }
    }

    fun update(id: String, params: Map<String, Any?>): Int {
        val data = params.map { (k, v) ->
            table.getEntityFieldByFieldName(k)?.takeIf { it.field.name != "id" && it.mutable }
                ?.run { this.column to v }
        }.filterNotNull()
        if (data.isEmpty()) return 0
        val selector = AutoFillSelector()
        return databaseService.primary().update(table) {
            data.forEach { (column, value) ->
                set(column, value)
            }
            for (entityField in entityFields.filter { it.autoFill?.fillOnUpdate ?: false }) {
                set(
                    entityField.column,
                    selector.getCurrent(entityField.autoFill!!.type)
                )
            }
            where {
                it.id eq id
            }
        }
    }

    fun insert(e: E): String {
        val selector = AutoFillSelector()
        for (entityField in entityFields.filter { it.autoFill?.fillOnInsert ?: false }) {
            val current = selector.getCurrent(entityField.autoFill!!.type)
            entityField.field.set(e, current)
        }
        val id = databaseService.primary().insertAndGenerateKey(table) {
            entityFields.filter { it.field.name != "id" }
                .forEach {
                    set(it.column, it.field.get(e))
                }
        } as String
        table.setValueByFieldName(e, "id", id)
        return id
    }

    fun bulkInsert(es: List<E>): Int {
        val selector = AutoFillSelector()
        for (entityField in entityFields.filter { it.autoFill?.fillOnInsert ?: false }) {
            val current = selector.getCurrent(entityField.autoFill!!.type)
            for (e in es) {
                entityField.field.set(e, current)
            }
        }
        return databaseService.primary().bulkInsert(table) {
            for (e in es) {
                item {
                    entityFields.filter { it.field.name != "id" }
                        .forEach {
                            set(it.column, it.field.get(e))
                        }
                }
            }
        }
    }

    fun insertOrUpdate(e: E, vararg conflictColumns: Column<*>): Int {
        val selector = AutoFillSelector()
        for (entityField in entityFields.filter { it.autoFill != null }) {
            val current = selector.getCurrent(entityField.autoFill!!.type)
            entityField.field.set(e, current)
        }
        val idEntityField = table.getEntityFieldByFieldName("id")!!

        return databaseService.primary().insertOrUpdate(table) {
            entityFields.filter { it.field.name != "id" }
                .forEach {
                    set(it.column, it.field.get(e))
                }
            if (conflictColumns.isEmpty()) {
                set(idEntityField.column, idEntityField.field.get(e))
            }
            onConflict(*conflictColumns) {
                entityFields.filter { it.field.name != "id" }
                    .forEach {
//                        冲突时不对字段非null但值为null的字段设置值
                        val value = it.field.get(e)
                        if (!it.nullable && value == null) return@forEach
                        set(it.column, value)
                    }
            }
        }

    }

    fun bulkInsertOrUpdate(es: List<E>, vararg conflictColumns: Column<*>): Int {
        if (conflictColumns.isEmpty()) throw IllegalArgumentException("conflictColumns can not be empty")
        val selector = AutoFillSelector()
        for (entityField in entityFields.filter { it.autoFill != null }) {
            val current = selector.getCurrent(entityField.autoFill!!.type)
            for (e in es) {
                entityField.field.set(e, current)
            }
        }
        val fields = entityFields.filter { it.field.name != "id" }
            .filter {
                val e = es[0]
                !(!it.nullable && it.field.get(e) == null)
            }

        return databaseService.primary().bulkInsertOrUpdate(table) {
            for (e in es) {
                item {
                    fields.forEach {
                        set(it.column, it.field.get(e))
                    }
                }
            }
            onConflict(*conflictColumns) {
                fields.forEach {
                    setExclude(it.column as Column<Any>)
//                    setc(it.column as Column<Any>, excluded(it.column))
                }
            }
        }
    }

    fun selectJson(
        columns: List<Column<*>> = table.columns,
        condition: (() -> ColumnDeclaring<Boolean>)? = null,
        orderBy: List<OrderByExpression> = emptyList(),
    ): MutableList<Json> {
        var query = databaseService.db().from(table)
            .select(columns)
        if (condition != null) {
            query = query.where(condition)
        }
        var order = orderBy
        if (order.isEmpty() && table.containsField("createDate")) {
            order = listOf(table["create_date"].desc())
        }

        return query
            .orderBy(order)
            .pageableMap {
                val json = Json()
                for (column in columns) {
                    val entityField =
                        table.getEntityFieldByColumnName(column.name) ?: continue
                    json[entityField.field.name] = it[column]
                }
                json
            }
    }

    fun select(
        condition: ((T) -> ColumnDeclaring<Boolean>)? = null,
        orderBy: List<OrderByExpression> = defaultOrder,
    ): MutableList<E> {
        var query = databaseService.db().from(table)
            .select(table.columns)
        if (condition != null) {
            query = query.where { condition.invoke(table) }
        }

        return query
            .orderBy(orderBy)
            .pageableMap { table.createEntity(it) }
    }

//    fun select(
//        columns: List<Column<*>> = table.columns,
//        orderBy: List<OrderByExpression> = emptyList(),
//    ): MutableList<MutableMap<String, Any?>> {
//        return select(columns,null, orderBy)
//    }
//
//    fun select(
//        vararg columns: Column<*>,
//        orderBy: List<OrderByExpression> = emptyList(),
//    ): MutableList<MutableMap<String, Any?>> {
//        return select(columns.toList(),null, orderBy)
//    }

//    fun singleRowSelect(
//        vararg conditions: ColumnDeclaring<Boolean>,
//        orderBy: List<OrderByExpression> = emptyList(),
//    ): E? {
//        return select(
//            conditions.takeIf { it.isNotEmpty() }?.run { { reduce(and) } },
//            orderBy
//        ).firstOrNull()
//    }
//
//    fun singleRowSelect(
//        condition: (T) -> ColumnDeclaring<Boolean>,
//        orderBy: List<OrderByExpression> = emptyList(),
//    ): E? {
//        return select(condition, orderBy)
//            .firstOrNull()
//    }
//
//    fun selectAll(
//        orderBy: List<OrderByExpression> = emptyList(),
//    ): MutableList<E> {
//        return select(null, orderBy)
//    }

    fun selectById(id: String): E? {
        return databaseService.db().from(table)
            .select(table.columns)
            .where {
                table.id eq id
            }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIdThrowIfNotExist(id: String): E {
        return selectById(id)
            ?: throw DataNotFoundException("${table.entityClass!!.simpleName} [id:$id]")
    }

    fun selectByIds(ids: Collection<String>): MutableList<E> {
        return databaseService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun count(
        column: Column<*>? = null,
        condition: ((T) -> ColumnDeclaring<Boolean>)? = null,
    ): Long {
        var query = databaseService.db().from(table).select(ktormDslCount(column))
        if (condition != null) {
            query = query.where { condition.invoke(table) }
        }
        return query.asIterable().first().getLong(1)
    }

    fun exist(id: String, throwException: Boolean = false): Boolean {
        val res = count(null, { table.id eq id }).toInt() == 1
        if (throwException && !res) throw DataNotFoundException("${table.entityClass!!.simpleName} [id:$id]")
        return res
    }

    fun <R> Query.pageableMap(transform: (row: QueryRowSet) -> R): MutableList<R> {
        var query = this

        val total = PageUtil.pageable.get()?.let { (offset, limit, needCount) ->
            var count: Long? = null
            if (needCount) {
                databaseService.db().let { db ->
                    db.useConnection {
                        val (sql, params) = db.formatExpression(this.expression)
                        it.execute(
                            "select count(*) count from (${sql}) t",
                            *params.map(ArgumentExpression<*>::value).toTypedArray()
                        ) { rs ->
                            rs?.apply {
                                next()
                                count = getLong(1)
                            }
                        }
                    }
                }
                if (count == 0L) {
                    return Page.empty()
                }
            }
            query = this.limit(offset, limit)
            count
        }

        val destination = ArrayList<R>()

        for (row in query) destination += transform(row)
        total?.let {
            return Page(it, destination)
        }
        return destination
    }

    inner class AutoFillSelector {
        private val session = Session.getInstance()
        private val now = LocalDateTime.now()
        fun getCurrent(type: AutoFillType): Any {
            return when (type) {
                ACCOUNT_ID -> session.accountId
                ACCOUNT_NAME -> session.accountName
                DATETIME -> now
                ROLE_ID -> session.roleId
                ROLE_NAME -> session.roleName
                ORG_ID -> session.orgId
                ORG_NAME -> session.orgName
            }
        }
    }
}