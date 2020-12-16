package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BizObjectMapping
import github.afezeria.hymn.core.module.table.CoreBizObjectMappings
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
class BizObjectMappingDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreBizObjectMappings()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectMapping): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.sourceBizObjectId, e.sourceBizObjectId)
            set(it.sourceTypeId, e.sourceTypeId)
            set(it.targetBizObjectId, e.targetBizObjectId)
            set(it.targetTypeId, e.targetTypeId)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectMapping): String {
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
            set(it.sourceBizObjectId, e.sourceBizObjectId)
            set(it.sourceTypeId, e.sourceTypeId)
            set(it.targetBizObjectId, e.targetBizObjectId)
            set(it.targetTypeId, e.targetTypeId)
        } as String
    }

    fun selectAll(): List<BizObjectMapping> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectMapping? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectBySourceBizObjectId(
        sourceBizObjectId: String,
    ): List<BizObjectMapping> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.sourceBizObjectId eq sourceBizObjectId
            }.map { table.createEntity(it) }
    }


}