package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.entity.BizObjectTrigger
import github.afezeria.hymn.core.module.table.CoreBizObjectTriggers
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
@Component
class BizObjectTriggerDao {

    @Autowired
    private lateinit var dbService: DatabaseService

    @Autowired
    private lateinit var  sessionService: SessionService

    val table = CoreBizObjectTriggers()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectTrigger): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session =  sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.active, e.active)
            set(it.remark, e.remark)
            set(it.bizObjectId, e.bizObjectId)
            set(it.name, e.name)
            set(it.api, e.api)
            set(it.lang, e.lang)
            set(it.optionText, e.optionText)
            set(it.ord, e.ord)
            set(it.event, e.event)
            set(it.code, e.code)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectTrigger): String {
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
            set(it.active, e.active)
            set(it.remark, e.remark)
            set(it.bizObjectId, e.bizObjectId)
            set(it.name, e.name)
            set(it.api, e.api)
            set(it.lang, e.lang)
            set(it.optionText, e.optionText)
            set(it.ord, e.ord)
            set(it.event, e.event)
            set(it.code, e.code)
            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
        } as String
    }

    fun selectAll(): MutableList<BizObjectTrigger> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectTrigger? {
        return dbService.db().from(table)
            .select(table.columns)
            .where { table.id eq id }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<BizObjectTrigger> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectTrigger? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
                table.api eq api
            }.mapTo(ArrayList()) { table.createEntity(it) }
            .firstOrNull()
    }


}