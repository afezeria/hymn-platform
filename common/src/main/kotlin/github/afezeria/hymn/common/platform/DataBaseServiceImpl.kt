package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.aspect.ReadOnlyInterceptor
import org.ktorm.database.Database
import org.ktorm.database.Transaction
import org.springframework.stereotype.Service


/**
 * @author afezeria
 */
@Service
class DataBaseServiceImpl(
    databaseList: List<Database>,
) : DatabaseService {

    private val writeable: Database
    private val iterator: Iterator<Database>?

    init {
        writeable = databaseList[0]
        iterator = when (databaseList.size) {
            1 -> null
            2 -> sequence {
                val database = databaseList[1]
                while (true) yield(database)
            }.iterator()
            else -> sequence {
                val slice = databaseList.slice(1 until databaseList.size)
                var i = 0
                while (true) {
                    yield(slice[i])
                    i++
                    if (i == slice.size) i = 0
                }
            }.iterator()
        }
    }

    override fun db(): Database {
        writeable.useTransaction { }
        return if (ReadOnlyInterceptor.isReadOnly()) {
            iterator?.next() ?: writeable
        } else {
            writeable
        }
    }

    override fun primary(): Database {
        return writeable
    }

    override fun readOnly(): Database {
        return iterator?.next() ?: writeable
    }

    override fun <T> useTransaction(fn: (Transaction) -> T): T {
        return writeable.useTransaction(func = fn)
    }
}