package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BizObjectTypeOptions
import github.afezeria.hymn.core.module.table.CoreBizObjectTypeOptionss
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.ktorm.dsl.*
import java.time.LocalDateTime
import java.util.*

/**
* @author afezeria
*/
@Component
class BizObjectTypeOptionsDao {

    @Autowired
    private lateinit var dbService: DataBaseService
    @Autowired
    private lateinit var sessionService:SessionService

    val table = CoreBizObjectTypeOptionss()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectTypeOptions): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.bizObjectId, e.bizObjectId)
            set(it.typeId, e.typeId)
            set(it.fieldId, e.fieldId)
            set(it.dictItemId, e.dictItemId)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectTypeOptions): String {
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
            set(it.typeId, e.typeId)
            set(it.fieldId, e.fieldId)
            set(it.dictItemId, e.dictItemId)
        } as String
    }

    fun selectAll(): List<BizObjectTypeOptions> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectTypeOptions? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): List<BizObjectTypeOptions> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
            }.map { table.createEntity(it) }
    }

    fun selectByTypeId(
        typeId: String,
    ): List<BizObjectTypeOptions> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.typeId eq typeId
            }.map { table.createEntity(it) }
    }


}