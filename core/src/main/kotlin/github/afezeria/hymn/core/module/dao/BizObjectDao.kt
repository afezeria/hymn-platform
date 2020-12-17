package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.BizObject
import github.afezeria.hymn.core.module.table.CoreBizObjects
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
class BizObjectDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreBizObjects()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObject): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.name, e.name)
            set(it.api, e.api)
            set(it.sourceTable, e.sourceTable)
            set(it.active, e.active)
            set(it.type, e.type)
            set(it.remoteUrl, e.remoteUrl)
            set(it.remoteToken, e.remoteToken)
            set(it.moduleApi, e.moduleApi)
            set(it.remark, e.remark)
            set(it.canInsert, e.canInsert)
            set(it.canUpdate, e.canUpdate)
            set(it.canDelete, e.canDelete)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObject): String {
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
            set(it.name, e.name)
            set(it.api, e.api)
            set(it.sourceTable, e.sourceTable)
            set(it.active, e.active)
            set(it.type, e.type)
            set(it.remoteUrl, e.remoteUrl)
            set(it.remoteToken, e.remoteToken)
            set(it.moduleApi, e.moduleApi)
            set(it.remark, e.remark)
            set(it.canInsert, e.canInsert)
            set(it.canUpdate, e.canUpdate)
            set(it.canDelete, e.canDelete)
        } as String
    }

    fun selectAll(): List<BizObject> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): BizObject? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByApi(
        api: String,
    ): BizObject? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.api eq api
            }.map { table.createEntity(it) }
            .firstOrNull()
    }


}