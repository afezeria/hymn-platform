package github.afezeria.hymn.common.util

import mu.KotlinLogging
import org.intellij.lang.annotations.Language
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.*
import java.time.*

/**
 * @author afezeria
 */

private val logger = KotlinLogging.logger {}

/**
 * 执行sql，支持命名参数
 * @param sql 待执行sql语句，命名参数，格式为 #{param}
 * @param params 参数哈希
 */
fun Connection.execute(
    @Language("sql") sql: String,
    params: Map<String, Any?>
): MutableList<MutableMap<String, Any?>> {
    return parseNamingSql(sql, params).run {
        execute(first, second)
    }
}


/**
 * 执行sql，支持 ? 占位符
 * @param sql 待执行sql语句
 * @param params 参数列表
 */
fun Connection.execute(
    @Language("sql") sql: String,
    params: Collection<Any?>
): MutableList<MutableMap<String, Any?>> {
    return execute(sql, *params.toTypedArray())
}

/**
 * 执行sql，支持 ? 占位符
 * @param sql 待执行sql语句
 * @param params 参数
 */
fun Connection.execute(
    @Language("sql") sql: String,
    vararg params: Any?
): MutableList<MutableMap<String, Any?>> {
    var result: MutableList<MutableMap<String, Any?>>? = null
    execute(sql, *params) {
        result = it.toList()
    }
    return requireNotNull(result)
}


fun Connection.execute(
    @Language("sql") sql: String,
    vararg params: Any?,
    handler: (ResultSet?) -> Unit
) {
    if (params.isNotEmpty()) {
        prepareStatement(sql).use {
            params.forEachIndexed { index, any ->
                when (any) {
                    is Array<*> -> {
                        if (any.isEmpty()) throw IllegalArgumentException("cannot use an empty array as a JDBC parameter")
                        val jdbcType = javaObject2JdbcType(any[0]!!)
                        if (jdbcType.isEmpty()) throw IllegalArgumentException("cannot convert $any to JDBC Array")
                        val jdbcArray = createArrayOf(jdbcType, any)
                        it.setArray(index + 1, jdbcArray)
                    }
                    is Collection<*> -> {
                        if (any.isEmpty()) throw IllegalArgumentException("cannot use an empty collection as a JDBC parameter")
                        val jdbcType = javaObject2JdbcType(any.first()!!)
                        if (jdbcType.isEmpty()) throw IllegalArgumentException("cannot convert $any to JDBC Array")
                        val jdbcArray = createArrayOf(jdbcType, any.toTypedArray())
                        it.setArray(index + 1, jdbcArray)
                    }
                    else -> {
                        it.setObject(index + 1, any)
                    }
                }
            }
            try {
                it.execute()
            } finally {
                if (logger.isDebugEnabled) {
                    var warnings = it.warnings
                    while (warnings != null) {
                        logger.debug(warnings.message)
                        warnings = warnings.nextWarning
                    }
                }
            }
            handler(it.resultSet)
        }
    } else {
        createStatement().use {
            try {
                it.execute(sql)
            } finally {
                if (logger.isDebugEnabled) {
                    var warnings = it.warnings
                    while (warnings != null) {
                        logger.debug(warnings.message)
                        warnings = warnings.nextWarning
                    }
                }
            }
            handler(it.resultSet)
        }
    }
}


fun ResultSet?.toList(): MutableList<MutableMap<String, Any?>> {
    if (this == null) return mutableListOf()
    val metaData = this.metaData
    val list = mutableListOf<MutableMap<String, Any?>>()
    while (next()) {
        val map = mutableMapOf<String, Any?>()
        (1..metaData.columnCount).forEach {
            val any = getObject(it)
            map[metaData.getColumnName(it)] = when (any) {
                null -> null
                is Time -> any.toLocalTime()
                is Date -> any.toLocalDate()
                is Timestamp -> any.toLocalDateTime()
                else -> any
            }
        }
        list.add(map)
    }
    return list
}

/**
 * 解析命名sql，将sql中的命名参数转换为标准站位符 "?"，并将参数值按顺序返回
 * @param sql 命名sql
 * @param params 参数map
 */
internal fun parseNamingSql(sql: String, params: Map<String, Any?>): Pair<String, List<Any?>> {
    if (sql.isBlank()) return sql to emptyList()
    val realSql = StringBuilder()
    val values = mutableListOf<Any?>()
    var i = 0
    while (true) {
        if (sql.length <= i) break
        if (sql[i] == '\\') {
            when (sql[i + 1]) {
                '\\' -> realSql.append("\\")
                '#' -> realSql.append("#")
                else -> realSql.append(sql[i]).append(sql[i + 1])
            }
            i += 2
            continue
        }
        if (sql[i] == '#'
            && sql.length > i + 3
            && sql[i + 1] == '{'
        ) {
            val tail = sql.indexOf("}", i + 2)
            if (tail == -1) {
                realSql.append(sql[i]).append(sql[i + 1])
                i += 2
                continue
            } else {
                val key = sql.substring(i + 2, sql.indexOf("}", i + 2))
                if (params.containsKey(key).not()) {
                    throw RuntimeException("missing parameter in sql:\"$sql\", params map does not contain key: $key")
                }
                values.add(params[key])
                realSql.append("?")
                i = tail + 1
                continue
            }
        }
        realSql.append(sql[i])
        i++
    }
    return realSql.toString() to values
}

private fun javaObject2JdbcType(any: Any): String {
    return when (any) {
        is String -> "VARCHAR"
        is BigDecimal -> "NUMERIC"
        is Boolean -> "BOOLEAN"
        is Byte -> "TINYINT"
        is Short -> "SMALLINT"
        is Int -> "INTEGER"
        is Long -> "BIGINT"
        is Float -> "REAL"
        is Double -> "DOUBLE"
        is ByteArray -> "BINARY"
        is BigInteger -> "BIGINT"
        is LocalDateTime -> "TIMESTAMP"
        is LocalDate -> "DATE"
        is LocalTime -> "TIME"
        is OffsetTime -> "TIME_WITH_TIMEZONE"
        is OffsetDateTime -> "TIMESTAMP_WITH_TIMEZONE"
        else -> ""
    }
}