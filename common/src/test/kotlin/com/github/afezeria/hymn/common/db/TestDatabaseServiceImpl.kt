package com.github.afezeria.hymn.common.db

import com.github.afezeria.hymn.common.ds1
import com.github.afezeria.hymn.common.platform.DatabaseService
import org.ktorm.database.Database
import org.ktorm.database.Transaction
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel

/**
 * @author afezeria
 */
object TestDatabaseServiceImpl : DatabaseService {
    val database = Database.connect(ds1, logger = ConsoleLogger(LogLevel.DEBUG))
    override fun db(): Database {
        return database
    }

    override fun primary(): Database {
        return database
    }

    override fun readOnly(): Database {
        return database
    }

    override fun user(): Database {
        return database
    }

    override fun <T> useTransaction(fn: (Transaction) -> T): T {
        return database.useTransaction(func = fn)
    }
}