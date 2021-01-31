package github.afezeria.hymn.common.db

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.util.execute
import org.ktorm.dsl.*
import org.ktorm.expression.BinaryExpression
import org.ktorm.expression.OrderByExpression
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring
import java.time.LocalDate
import java.time.LocalDateTime
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
    }

    fun delete(condition: (T) -> ColumnDeclaring<Boolean>): Int {
        return databaseService.primary().delete(table, condition)
    }

    fun deleteById(id: String): Int {
        return databaseService.primary().delete(table) {
            it.id eq id
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
            table.getEntityFieldList().asSequence().filter { it.field.name != "id" }
                .forEach { field ->
                    set(field.column, field.field.get(e))
                }
            where {
                it.id eq e.id
            }
        }
    }

    fun update(id: String, data: Map<String, Any?>): Int {
        val selector = AutoFillSelector()
        return databaseService.primary().update(table) {
            if (data.size > table.fieldCount) {
                for (entityField in entityFields) {
                    if (data.containsKey(entityField.field.name)) {
                        set(entityField.column, data[entityField.field.name])
                    }
                }
            } else {
                for (entry in data.entries) {
                    table.getColumnByFieldName(entry.key)?.also {
                        set(it, entry.value)
                    }
                }
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
            table.getEntityFieldList().asSequence().filter { it.field.name != "id" }
                .forEach {
                    set(it.column, it.field.get(e))
                }
        } as String
        table.setValueByFieldName(e, "id", id)
        return id
    }

    fun select(
        columns: List<Column<*>>,
        condition: (() -> ColumnDeclaring<Boolean>)? = null,
        offset: Int? = null,
        limit: Int? = null,
        orderBy: List<OrderByExpression> = emptyList(),
    ): MutableList<MutableMap<String, Any?>> {
        var query = databaseService.db().from(table)
            .select(columns)
        if (condition != null) {
            query = query.where(condition)
        }
        return query.limit(offset, limit)
            .orderBy(orderBy)
            .mapTo(ArrayList()) {
                val map: MutableMap<String, Any?> = mutableMapOf()
                for (column in columns) {
                    val entityField =
                        table.getEntityFieldByColumnName(column.name) ?: continue
                    map[entityField.field.name] = it[column]
                }
                map
            }
    }

    fun select(
        condition: (() -> ColumnDeclaring<Boolean>)? = null,
        offset: Int? = null,
        limit: Int? = null,
        orderBy: List<OrderByExpression> = emptyList(),
    ): MutableList<E> {
        var query = databaseService.db().from(table)
            .select(table.columns)
        if (condition != null) {
            query = query.where(condition)
        }
        return query.limit(offset, limit)
            .orderBy(orderBy)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun singleRowSelect(
        condition: () -> ColumnDeclaring<Boolean>,
        orderBy: List<OrderByExpression> = emptyList(),
    ): E? {
        return select(condition, 0, 1, orderBy)
            .firstOrNull()
    }

    fun select(
        conditions: List<ColumnDeclaring<Boolean>> = emptyList(),
        offset: Int? = null,
        limit: Int? = null,
        orderBy: List<OrderByExpression> = emptyList(),
    ): MutableList<E> {
        return select(
            conditions.takeIf { it.isNotEmpty() }?.run { { reduce(and) } },
            limit,
            offset,
            orderBy
        )
    }

    fun pageSelect(
        condition: (() -> ColumnDeclaring<Boolean>)? = null,
        pageSize: Int,
        pageNumber: Int,
        orderBy: List<OrderByExpression> = emptyList(),
    ): MutableList<E> {
        if (pageSize < 1) throw IllegalArgumentException("pageSize must be greater than 0, current value $pageSize")
        if (pageNumber < 1) throw IllegalArgumentException("pageNumber must be greater than 0, current value $pageNumber")
        return select(condition, (pageNumber - 1) * pageSize, pageSize, orderBy)
    }

    fun selectAll(): MutableList<E> {
        return databaseService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

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

    fun selectByIds(ids: List<String>): MutableList<E> {
        return databaseService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun count(
        column: Column<*>? = null,
        condition: (() -> ColumnDeclaring<Boolean>)? = null
    ): Long {
        var query = databaseService.db().from(table).select(ktormDslCount(column))
        if (condition != null) {
            query = query.where(condition)
        }
        return query.asIterable().first().getLong(1)
    }

    fun history(
        id: String,
        startTime: LocalDateTime? = null,
        endTime: LocalDateTime = LocalDateTime.now()
    ): MutableList<MutableMap<String, Any?>> {
        if (!table.hasHistory()) throw NotImplementedError("table ${table.schema}.${table.tableName} has no history")
        databaseService.db().useConnection {
            val qualifiedTableName = "${table.schema}.\"${table.tableName}_history\""
            return if (startTime != null) {
                it.execute(
                    "select * from $qualifiedTableName where id = ? and stamp between ? and ?",
                    id,
                    startTime,
                    endTime
                )
            } else {
                it.execute(
                    "select * from $qualifiedTableName where id = ? and stamp < ?",
                    id,
                    endTime
                )
            }
        }
    }

    inner class AutoFillSelector {
        private val session = Session.getInstance()
        private val now = LocalDateTime.now()
        private val today = LocalDate.now()
        fun getCurrent(type: AutoFillType): Any {
            return when (type) {
                AutoFillType.ACCOUNT_ID -> session.accountId
                AutoFillType.ACCOUNT_NAME -> session.accountName
                AutoFillType.DATE -> today
                AutoFillType.DATETIME -> now
                AutoFillType.ROLE_ID -> session.roleId
                AutoFillType.ROLE_NAME -> session.roleName
                AutoFillType.ORG_ID -> session.orgId
                AutoFillType.ORG_NAME -> session.orgName
            }
        }
    }
}