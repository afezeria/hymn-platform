package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import github.afezeria.hymn.core.module.table.CoreBizObjectFieldPerms
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
class BizObjectFieldPermDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreBizObjectFieldPerms()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectFieldPerm): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.roleId, e.roleId)
            set(it.bizObjectId, e.bizObjectId)
            set(it.fieldId, e.fieldId)
            set(it.pRead, e.pRead)
            set(it.pEdit, e.pEdit)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectFieldPerm): String {
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
            set(it.bizObjectId, e.bizObjectId)
            set(it.fieldId, e.fieldId)
            set(it.pRead, e.pRead)
            set(it.pEdit, e.pEdit)
        } as String
    }

    fun selectAll(): List<BizObjectFieldPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectFieldPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
                table.fieldId eq fieldId
            }.map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleId(
        roleId: String,
    ): List<BizObjectFieldPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.map { table.createEntity(it) }
    }

    fun selectByFieldId(
        fieldId: String,
    ): List<BizObjectFieldPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.fieldId eq fieldId
            }.map { table.createEntity(it) }
    }


}