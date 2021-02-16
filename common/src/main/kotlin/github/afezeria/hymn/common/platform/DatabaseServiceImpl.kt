package github.afezeria.hymn.common.platform

import com.p6spy.engine.spy.P6DataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import github.afezeria.hymn.common.aspect.ReadOnlyInterceptor
import github.afezeria.hymn.common.conf.DataBaseProperties
import mu.KLogging
import org.ktorm.database.Database
import org.ktorm.database.Transaction
import org.ktorm.logging.Slf4jLoggerAdapter
import org.springframework.stereotype.Service


/**
 * @author afezeria
 */
@Service
class DatabaseServiceImpl(
    dataBaseProperties: DataBaseProperties
) : DatabaseService {
    companion object : KLogging()

    private val writeable: Database
    private val iterator: Iterator<Database>
    private val user: Database

    init {
        if (dataBaseProperties.admin.isEmpty())
            throw IllegalArgumentException("缺少数据源配置")
        val adminDatabase = dataBaseProperties.admin.map {
            Database.connect(
                P6DataSource(HikariDataSource(HikariConfig(it))),
                logger = Slf4jLoggerAdapter(logger),
            )
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

    override fun db(): Database {
        return if (ReadOnlyInterceptor.isReadOnly()) {
            iterator.next()
        } else {
            writeable
        }
    }

    override fun primary(): Database {
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