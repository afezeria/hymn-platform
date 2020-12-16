package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.AccountObjectView
import github.afezeria.hymn.core.module.table.CoreAccountObjectViews
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
class AccountObjectViewDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreAccountObjectViews()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: AccountObjectView): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.copyId, e.copyId)
            set(it.remark, e.remark)
            set(it.globalView, e.globalView)
            set(it.defaultView, e.defaultView)
            set(it.accountId, e.accountId)
            set(it.bizObjectId, e.bizObjectId)
            set(it.name, e.name)
            set(it.viewJson, e.viewJson)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: AccountObjectView): String {
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
            set(it.copyId, e.copyId)
            set(it.remark, e.remark)
            set(it.globalView, e.globalView)
            set(it.defaultView, e.defaultView)
            set(it.accountId, e.accountId)
            set(it.bizObjectId, e.bizObjectId)
            set(it.name, e.name)
            set(it.viewJson, e.viewJson)
        } as String
    }

    fun selectAll(): List<AccountObjectView> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): AccountObjectView? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByAccountId(
        accountId: String,
    ): List<AccountObjectView> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.accountId eq accountId
            }.map { table.createEntity(it) }
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): List<AccountObjectView> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
            }.map { table.createEntity(it) }
    }


}