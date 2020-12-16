package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.Account
import github.afezeria.hymn.core.module.table.CoreAccounts
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
class AccountDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreAccounts()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: Account): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.lockTime, e.lockTime)
            set(it.name, e.name)
            set(it.username, e.username)
            set(it.password, e.password)
            set(it.onlineRule, e.onlineRule)
            set(it.active, e.active)
            set(it.admin, e.admin)
            set(it.root, e.root)
            set(it.leaderId, e.leaderId)
            set(it.orgId, e.orgId)
            set(it.roleId, e.roleId)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: Account): String {
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
            set(it.lockTime, e.lockTime)
            set(it.name, e.name)
            set(it.username, e.username)
            set(it.password, e.password)
            set(it.onlineRule, e.onlineRule)
            set(it.active, e.active)
            set(it.admin, e.admin)
            set(it.root, e.root)
            set(it.leaderId, e.leaderId)
            set(it.orgId, e.orgId)
            set(it.roleId, e.roleId)
        } as String
    }

    fun selectAll(): List<Account> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): Account? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByLeaderId(
        leaderId: String,
    ): List<Account> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.leaderId eq leaderId
            }.map { table.createEntity(it) }
    }

    fun selectByOrgId(
        orgId: String,
    ): List<Account> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.orgId eq orgId
            }.map { table.createEntity(it) }
    }

    fun selectByRoleId(
        roleId: String,
    ): List<Account> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.map { table.createEntity(it) }
    }


}