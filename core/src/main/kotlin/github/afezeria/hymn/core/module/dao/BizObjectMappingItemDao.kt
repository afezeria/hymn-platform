package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import github.afezeria.hymn.core.module.table.CoreBizObjectMappingItems
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
class BizObjectMappingItemDao {

    @Autowired
    private lateinit var dbService: DataBaseService
    @Autowired
    private lateinit var sessionService:SessionService

    val table = CoreBizObjectMappingItems()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectMappingItem): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.mappingId, e.mappingId)
            set(it.sourceFieldId, e.sourceFieldId)
            set(it.targetFieldId, e.targetFieldId)
            set(it.refField1Id, e.refField1Id)
            set(it.refField1BizObjectId, e.refField1BizObjectId)
            set(it.refField2Id, e.refField2Id)
            set(it.refField2BizObjectId, e.refField2BizObjectId)
            set(it.refField3Id, e.refField3Id)
            set(it.refField3BizObjectId, e.refField3BizObjectId)
            set(it.refField4Id, e.refField4Id)
            set(it.refField4BizObjectId, e.refField4BizObjectId)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectMappingItem): String {
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
            set(it.mappingId, e.mappingId)
            set(it.sourceFieldId, e.sourceFieldId)
            set(it.targetFieldId, e.targetFieldId)
            set(it.refField1Id, e.refField1Id)
            set(it.refField1BizObjectId, e.refField1BizObjectId)
            set(it.refField2Id, e.refField2Id)
            set(it.refField2BizObjectId, e.refField2BizObjectId)
            set(it.refField3Id, e.refField3Id)
            set(it.refField3BizObjectId, e.refField3BizObjectId)
            set(it.refField4Id, e.refField4Id)
            set(it.refField4BizObjectId, e.refField4BizObjectId)
        } as String
    }

    fun selectAll(): List<BizObjectMappingItem> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectMappingItem? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }


}