package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.entity.Account
import github.afezeria.hymn.core.module.table.CoreAccounts
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
@Component
class AccountDao {

    @Autowired
    private lateinit var dbService: DatabaseService

    @Autowired
    private lateinit var  sessionService: SessionService

    val table = CoreAccounts()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: Account): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session =  sessionService.getSession()
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
            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
        } as String
    }

    fun selectAll(): MutableList<Account> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): Account? {
        return dbService.db().from(table)
            .select(table.columns)
            .where { table.id eq id }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<Account> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByLeaderId(
        leaderId: String,
    ): MutableList<Account> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.leaderId eq leaderId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByOrgId(
        orgId: String,
    ): MutableList<Account> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.orgId eq orgId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<Account> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }


}