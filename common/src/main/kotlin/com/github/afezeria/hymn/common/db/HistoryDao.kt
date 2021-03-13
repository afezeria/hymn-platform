package com.github.afezeria.hymn.common.db

import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.execute
import java.time.LocalDateTime

/**
 * @author afezeria
 */
interface HistoryDao {
    val databaseService: DatabaseService
    val table: AbstractTable<*>

    fun history(
        id: String,
        startTime: LocalDateTime? = null,
        endTime: LocalDateTime = LocalDateTime.now()
    ): MutableList<MutableMap<String, Any?>> {
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
}
