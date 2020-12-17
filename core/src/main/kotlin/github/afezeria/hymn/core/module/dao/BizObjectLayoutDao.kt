package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BizObjectLayout
import github.afezeria.hymn.core.module.table.CoreBizObjectLayouts
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
class BizObjectLayoutDao {

    @Autowired
    private lateinit var dbService: DataBaseService
    @Autowired
    private lateinit var sessionService:SessionService

    val table = CoreBizObjectLayouts()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectLayout): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.bizObjectId, e.bizObjectId)
            set(it.name, e.name)
            set(it.remark, e.remark)
            set(it.relFieldJsonArr, e.relFieldJsonArr)
            set(it.pcReadLayoutJson, e.pcReadLayoutJson)
            set(it.pcEditLayoutJson, e.pcEditLayoutJson)
            set(it.mobileReadLayoutJson, e.mobileReadLayoutJson)
            set(it.mobileEditLayoutJson, e.mobileEditLayoutJson)
            set(it.previewLayoutJson, e.previewLayoutJson)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectLayout): String {
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
            set(it.remark, e.remark)
            set(it.relFieldJsonArr, e.relFieldJsonArr)
            set(it.pcReadLayoutJson, e.pcReadLayoutJson)
            set(it.pcEditLayoutJson, e.pcEditLayoutJson)
            set(it.mobileReadLayoutJson, e.mobileReadLayoutJson)
            set(it.mobileEditLayoutJson, e.mobileEditLayoutJson)
            set(it.previewLayoutJson, e.previewLayoutJson)
        } as String
    }

    fun selectAll(): MutableList<BizObjectLayout> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectLayout? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<BizObjectLayout>{
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectLayout? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
                table.name eq name
            }.mapTo(ArrayList()) { table.createEntity(it) }
            .firstOrNull()
    }


}