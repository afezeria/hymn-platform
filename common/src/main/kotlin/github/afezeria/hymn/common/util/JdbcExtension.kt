package github.afezeria.hymn.common.util

import mu.KotlinLogging
import org.intellij.lang.annotations.Language
import java.sql.*

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
    params: List<Any?>
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
    return if (params.isNotEmpty()) {
        prepareStatement(sql).use {
            params.forEachIndexed { index, any ->
                it.setObject(index + 1, any)
            }
            it.execute()
            if (logger.isDebugEnabled) {
                var warnings = it.warnings
                while (warnings != null) {
                    logger.debug(warnings.message)
                    warnings = warnings.nextWarning
                }
            }
            it.resultSet.toList()
        }
    } else {
        createStatement().use {
            it.execute(sql)
            if (logger.isDebugEnabled) {
                var warnings = it.warnings
                while (warnings != null) {
                    logger.debug(warnings.message)
                    warnings = warnings.nextWarning
                }
            }
            it.resultSet.toList()
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
