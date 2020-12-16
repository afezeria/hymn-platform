package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.DictItem
import github.afezeria.hymn.core.module.table.CoreDictItems
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
class DictItemDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreDictItems()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: DictItem): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.dictId, e.dictId)
            set(it.name, e.name)
            set(it.code, e.code)
            set(it.parentCode, e.parentCode)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: DictItem): String {
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
            set(it.dictId, e.dictId)
            set(it.name, e.name)
            set(it.code, e.code)
            set(it.parentCode, e.parentCode)
        } as String
    }

    fun selectAll(): List<DictItem> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): DictItem? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByDictIdAndCode(
        dictId: String,
        code: String,
    ): DictItem? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.dictId eq dictId
                table.code eq code
            }.map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByDictId(
        dictId: String,
    ): List<DictItem> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.dictId eq dictId
            }.map { table.createEntity(it) }
    }


}