package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.entity.CustomPage
import github.afezeria.hymn.core.module.table.CoreCustomPages
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
@Component
class CustomPageDao {

    @Autowired
    private lateinit var dbService: DatabaseService

    @Autowired
    private lateinit var  sessionService: SessionService

    val table = CoreCustomPages()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

   fun update(e: CustomPage): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        e.modifyDate = LocalDateTime.now()
        e.modifyById = session.accountId
        e.modifyBy = session.accountName
        return  dbService.db().update(table) {
            set(it.api, e.api)
            set(it.name, e.name)
            set(it.md5, e.md5)
            set(it.remark, e.remark)
            set(it.modifyById, e.modifyById)
            set(it.modifyBy, e.modifyBy)
            set(it.modifyDate, e.modifyDate)
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: CustomPage): String {
        val now = LocalDateTime.now()
        val session =  sessionService.getSession()
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
            set(it.md5, e.md5)
            set(it.remark, e.remark)
            set(it.createDate, e.createDate)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
        } as String
    }

    fun selectAll(): MutableList<CustomPage> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): CustomPage? {
        return dbService.db().from(table)
            .select(table.columns)
            .where { table.id eq id }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<CustomPage> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByApi(
        api: String,
    ): CustomPage? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.api eq api
            }.mapTo(ArrayList()) { table.createEntity(it) }
            .firstOrNull()
    }


}