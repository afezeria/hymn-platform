package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BizObjectType
import github.afezeria.hymn.core.module.table.CoreBizObjectTypes
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
class BizObjectTypeDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreBizObjectTypes()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectType): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.bizObjectId, e.bizObjectId)
            set(it.name, e.name)
            set(it.active, e.active)
            set(it.remark, e.remark)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectType): String {
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
            set(it.bizObjectId, e.bizObjectId)
            set(it.name, e.name)
            set(it.active, e.active)
            set(it.remark, e.remark)
        } as String
    }

    fun selectAll(): List<BizObjectType> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectType? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectType? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
                table.name eq name
            }.map { table.createEntity(it) }
            .firstOrNull()
    }


}