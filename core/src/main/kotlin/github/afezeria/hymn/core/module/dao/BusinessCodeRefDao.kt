package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import github.afezeria.hymn.core.module.table.CoreBusinessCodeRefs
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
class BusinessCodeRefDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreBusinessCodeRefs()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BusinessCodeRef): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.triggerId, e.triggerId)
            set(it.interfaceId, e.interfaceId)
            set(it.sharedCodeId, e.sharedCodeId)
            set(it.bizObjectId, e.bizObjectId)
            set(it.fieldId, e.fieldId)
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

    fun insert(e: BusinessCodeRef): String {
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
            set(it.triggerId, e.triggerId)
            set(it.interfaceId, e.interfaceId)
            set(it.sharedCodeId, e.sharedCodeId)
            set(it.bizObjectId, e.bizObjectId)
            set(it.fieldId, e.fieldId)
            set(it.orgId, e.orgId)
            set(it.roleId, e.roleId)
        } as String
    }

    fun selectAll(): List<BusinessCodeRef> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): BusinessCodeRef? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByFieldId(
        fieldId: String,
    ): List<BusinessCodeRef> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.fieldId eq fieldId
            }.map { table.createEntity(it) }
    }

    fun selectByOrgId(
        orgId: String,
    ): List<BusinessCodeRef> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.orgId eq orgId
            }.map { table.createEntity(it) }
    }

    fun selectByRoleId(
        roleId: String,
    ): List<BusinessCodeRef> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.map { table.createEntity(it) }
    }


}