package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.db.ReadOnlyInterceptor
import org.ktorm.database.Database
import org.springframework.stereotype.Service


/**
 * @author afezeria
 */
@Service
class DataBaseServiceImpl(
    databaseList: List<Database>,
) : DataBaseService {

    private val write: Database
    private val iterator: Iterator<Database>?

    init {
        write = databaseList[0]
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
        return if (ReadOnlyInterceptor.isReadOnly()) {
            iterator?.next() ?: write
        } else {
            write
        }
    }

    override fun readOnly(): Database {
        return iterator?.next() ?: write
    }
}