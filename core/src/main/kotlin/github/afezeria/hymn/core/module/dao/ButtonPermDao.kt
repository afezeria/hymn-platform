package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.ButtonPerm
import github.afezeria.hymn.core.module.table.CoreButtonPerms
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
class ButtonPermDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreButtonPerms()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: ButtonPerm): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.roleId, e.roleId)
            set(it.buttonId, e.buttonId)
            set(it.visible, e.visible)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: ButtonPerm): String {
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
            set(it.roleId, e.roleId)
            set(it.buttonId, e.buttonId)
            set(it.visible, e.visible)
        } as String
    }

    fun selectAll(): List<ButtonPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): ButtonPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleIdAndButtonId(
        roleId: String,
        buttonId: String,
    ): ButtonPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
                table.buttonId eq buttonId
            }.map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleId(
        roleId: String,
    ): List<ButtonPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.map { table.createEntity(it) }
    }

    fun selectByButtonId(
        buttonId: String,
    ): List<ButtonPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.buttonId eq buttonId
            }.map { table.createEntity(it) }
    }


}