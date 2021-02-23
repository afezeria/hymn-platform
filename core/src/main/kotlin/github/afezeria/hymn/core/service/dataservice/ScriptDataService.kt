package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.platform.dataservice.DataService
import github.afezeria.hymn.common.platform.dataservice.FieldInfo
import github.afezeria.hymn.common.util.execute
import mu.KLogger
import org.ktorm.database.Database
import java.time.LocalDateTime

/**
 * @author afezeria
 */
interface ScriptDataService : DataService {
    val logger: KLogger
    val database: Database

    fun execute(
        sql: String,
        params: Collection<Any?>,
        type: WriteType,
        objectApiName: String,
        oldData: MutableMap<String, Any?>? = null,
        newData: MutableMap<String, Any?>? = null,
        trigger: Boolean = true,
    ): MutableMap<String, Any?>

    override fun sql(sql: String, vararg params: Any?): MutableList<MutableMap<String, Any?>> {
        database.useConnection {
            return it.execute(sql, *params)
        }
    }

    override fun sql(sql: String, params: List<Any?>): MutableList<MutableMap<String, Any?>> {
        database.useConnection {
            return it.execute(sql, params)
        }
    }

    override fun sql(
        sql: String,
        params: Map<String, Any?>
    ): MutableList<MutableMap<String, Any?>> {
        database.useConnection {
            return it.execute(sql, params)
        }
    }

    fun checkNewDataValue(
        fields: FieldInfo,
        value: Any?,
        session: Session,
        now: LocalDateTime
    ): Any? {
        return value?.apply {

        }
    }

}