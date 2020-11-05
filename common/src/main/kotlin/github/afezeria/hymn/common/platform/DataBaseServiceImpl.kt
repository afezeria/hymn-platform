package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.db.ReadOnlyInterceptor
import org.ktorm.database.Database
import org.springframework.stereotype.Component
import javax.sql.DataSource
import kotlin.random.Random

/**
 * @author afezeria
 */

@Component
class DataBaseServiceImpl(
    dataSourceMap: MutableMap<String, DataSource>,
) : DataBaseService {

    private val databases: List<Database>
    private lateinit var default: Database
    private val random = Random(System.currentTimeMillis())

    init {
        val write = dataSourceMap.remove("write") ?: throw RuntimeException("缺少 write 数据源")
        default = Database.connectWithSpringSupport(write)
        databases = dataSourceMap.map {
            Database.connect(it.value)
        }
    }

    override fun db(): Database {
        return if (!ReadOnlyInterceptor.isReadOnly()) {
            default
        } else {
            if (databases.isNotEmpty()) {
                databases[random.nextInt(databases.size)]
            } else {
                default
            }
        }
    }
}