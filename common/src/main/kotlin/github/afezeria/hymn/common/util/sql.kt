package github.afezeria.hymn.common.util

import org.intellij.lang.annotations.Language
import java.sql.Connection
import java.sql.ResultSet

/**
 * @author afezeria
 */

fun Connection.execute(@Language("sql") sql: String, vararg params: Any?): MutableList<MutableMap<String, Any?>> {
    return if (params.isNotEmpty()) {
        prepareStatement(sql).use {
            params.forEachIndexed { index, any ->
                it.setObject(index + 1, any)
            }
            it.execute()
            it.resultSet.toList()
        }
    } else {
        createStatement().use {
            it.execute(sql)
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
            map[metaData.getColumnName(it)] = getObject(it)
        }
        list.add(map)
    }
    return list
}