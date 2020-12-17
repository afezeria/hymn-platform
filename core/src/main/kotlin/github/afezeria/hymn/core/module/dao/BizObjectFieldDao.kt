package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BizObjectField
import github.afezeria.hymn.core.module.table.CoreBizObjectFields
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
class BizObjectFieldDao {

    @Autowired
    private lateinit var dbService: DataBaseService
    @Autowired
    private lateinit var sessionService:SessionService

    val table = CoreBizObjectFields()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectField): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.sourceColumn, e.sourceColumn)
            set(it.bizObjectId, e.bizObjectId)
            set(it.name, e.name)
            set(it.api, e.api)
            set(it.type, e.type)
            set(it.active, e.active)
            set(it.history, e.history)
            set(it.defaultValue, e.defaultValue)
            set(it.formula, e.formula)
            set(it.maxLength, e.maxLength)
            set(it.minLength, e.minLength)
            set(it.visibleRow, e.visibleRow)
            set(it.dictId, e.dictId)
            set(it.masterFieldId, e.masterFieldId)
            set(it.optionalNumber, e.optionalNumber)
            set(it.refId, e.refId)
            set(it.refListLabel, e.refListLabel)
            set(it.refDeletePolicy, e.refDeletePolicy)
            set(it.queryFilter, e.queryFilter)
            set(it.sId, e.sId)
            set(it.sFieldId, e.sFieldId)
            set(it.sType, e.sType)
            set(it.genRule, e.genRule)
            set(it.remark, e.remark)
            set(it.help, e.help)
            set(it.joinViewName, e.joinViewName)
            set(it.standardType, e.standardType)
            set(it.isPredefined, e.isPredefined)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectField): String {
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
            set(it.sourceColumn, e.sourceColumn)
            set(it.bizObjectId, e.bizObjectId)
            set(it.name, e.name)
            set(it.api, e.api)
            set(it.type, e.type)
            set(it.active, e.active)
            set(it.history, e.history)
            set(it.defaultValue, e.defaultValue)
            set(it.formula, e.formula)
            set(it.maxLength, e.maxLength)
            set(it.minLength, e.minLength)
            set(it.visibleRow, e.visibleRow)
            set(it.dictId, e.dictId)
            set(it.masterFieldId, e.masterFieldId)
            set(it.optionalNumber, e.optionalNumber)
            set(it.refId, e.refId)
            set(it.refListLabel, e.refListLabel)
            set(it.refDeletePolicy, e.refDeletePolicy)
            set(it.queryFilter, e.queryFilter)
            set(it.sId, e.sId)
            set(it.sFieldId, e.sFieldId)
            set(it.sType, e.sType)
            set(it.genRule, e.genRule)
            set(it.remark, e.remark)
            set(it.help, e.help)
            set(it.joinViewName, e.joinViewName)
            set(it.standardType, e.standardType)
            set(it.isPredefined, e.isPredefined)
        } as String
    }

    fun selectAll(): MutableList<BizObjectField> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectField? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<BizObjectField>{
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectField? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
                table.api eq api
            }.mapTo(ArrayList()) { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectField> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }


}