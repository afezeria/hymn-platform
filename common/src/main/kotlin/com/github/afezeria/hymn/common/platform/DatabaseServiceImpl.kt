package com.github.afezeria.hymn.common.platform

import com.github.afezeria.hymn.common.aspect.ReadOnlyInterceptor
import com.github.afezeria.hymn.common.conf.DataBaseProperties
import com.p6spy.engine.spy.P6DataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KLogging
import org.ktorm.database.Database
import org.ktorm.database.Transaction
import org.ktorm.logging.Slf4jLoggerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import javax.sql.DataSource


/**
 * @author afezeria
 */
@Service
@Configuration
class DatabaseServiceImpl(
    dataBaseProperties: DataBaseProperties
) : DatabaseService {
    companion object : KLogging()

    private val writeable: Database
    private val iterator: Iterator<Database>
    private val user: Database
    private val primaryDataSource: DataSource

    init {
        if (dataBaseProperties.admin.isEmpty())
            throw IllegalArgumentException("缺少数据源配置")

        val datasourceList = dataBaseProperties.admin.map {
            P6DataSource(HikariDataSource(HikariConfig(it)))
        }
        primaryDataSource = datasourceList[0]

        val adminDatabase = datasourceList.map {
            Database.connect(it, logger = Slf4jLoggerAdapter(logger))
        }

        writeable = adminDatabase[0]
        iterator = object : Iterator<Database> {
            val dbs = if (adminDatabase.size > 1) {
                adminDatabase.slice(1 until adminDatabase.size)
            } else {
                adminDatabase.toList()
            }
            val size = dbs.size
            var idx = 0
            override fun hasNext(): Boolean = true


            override fun next(): Database {
                val db = dbs[idx]
                if (++idx == size) {
                    idx = 0
                }
                return db
            }
        }
        user = dataBaseProperties.user.takeIf { it.isNotEmpty() }?.let {
            Database.connect(
                P6DataSource(HikariDataSource(HikariConfig(it))),
                logger = Slf4jLoggerAdapter(logger),
            )
        } ?: adminDatabase[0]
    }

    @Bean
    fun datasource(): DataSource {
        return primaryDataSource
    }

    override fun db(): Database {
        return if (ReadOnlyInterceptor.isReadOnly()) {
            iterator.next()
        } else {
            writeable
        }
    }

    override fun primary(): Database {
        writeable
        return writeable
    }

    override fun readOnly(): Database {
        return iterator.next()
    }

    override fun user(): Database {
        return user
    }

    override fun <T> useTransaction(fn: (Transaction) -> T): T {
        return writeable.useTransaction(func = fn)
    }
}