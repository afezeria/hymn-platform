package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.CustomComponent
import github.afezeria.hymn.core.module.table.CoreCustomComponents
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
class CustomComponentDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreCustomComponents()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: CustomComponent): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.api, e.api)
            set(it.name, e.name)
            set(it.code, e.code)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: CustomComponent): String {
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
            set(it.api, e.api)
            set(it.name, e.name)
            set(it.code, e.code)
        } as String
    }

    fun selectAll(): List<CustomComponent> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): CustomComponent? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByApi(
        api: String,
    ): CustomComponent? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.api eq api
            }.map { table.createEntity(it) }
            .firstOrNull()
    }


}