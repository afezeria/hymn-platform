package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.CustomMenuItem
import github.afezeria.hymn.core.module.table.CoreCustomMenuItems
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import org.ktorm.dsl.*
import java.time.LocalDateTime
import java.util.*

/**
* @author afezeria
*/
@Component
class CustomMenuItemDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreCustomMenuItems()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: CustomMenuItem): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.name, e.name)
            set(it.path, e.path)
            set(it.pathType, e.pathType)
            set(it.action, e.action)
            set(it.clientType, e.clientType)
            set(it.icon, e.icon)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: CustomMenuItem): String {
        val now = LocalDateTime.now()
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        e.createDate = now
        e.modifyDate = now
        e.createById = accountId
        e.modifyById = accountId
        e.createBy = accountName
        e.modifyBy = accountName
        return dbService.db().insertAndGenerateKey(table) {
            set(it.name, e.name)
            set(it.path, e.path)
            set(it.pathType, e.pathType)
            set(it.action, e.action)
            set(it.clientType, e.clientType)
            set(it.icon, e.icon)
        } as String
    }

    fun selectAll(): List<CustomMenuItem> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): CustomMenuItem? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }


}