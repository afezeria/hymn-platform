package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout
import github.afezeria.hymn.core.module.table.CoreBizObjectTypeLayouts
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
class BizObjectTypeLayoutDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreBizObjectTypeLayouts()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectTypeLayout): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.roleId, e.roleId)
            set(it.bizObjectId, e.bizObjectId)
            set(it.typeId, e.typeId)
            set(it.layoutId, e.layoutId)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectTypeLayout): String {
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
            set(it.typeId, e.typeId)
            set(it.layoutId, e.layoutId)
        } as String
    }

    fun selectAll(): List<BizObjectTypeLayout> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectTypeLayout? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleIdAndTypeIdAndLayoutId(
        roleId: String,
        typeId: String,
        layoutId: String,
    ): BizObjectTypeLayout? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
                table.typeId eq typeId
                table.layoutId eq layoutId
            }.map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleId(
        roleId: String,
    ): List<BizObjectTypeLayout> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.map { table.createEntity(it) }
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): List<BizObjectTypeLayout> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
            }.map { table.createEntity(it) }
    }


}