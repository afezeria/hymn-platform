package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import github.afezeria.hymn.core.module.table.CoreBizObjectFieldPerms
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertOrUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
@Component
class BizObjectFieldPermDao {

    @Autowired
    private lateinit var dbService: DataBaseService

    @Autowired
    private lateinit var  sessionService: SessionService

    val table = CoreBizObjectFieldPerms()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectFieldPerm): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session =  sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.roleId, e.roleId)
            set(it.fieldId, e.fieldId)
            set(it.pRead, e.pRead)
            set(it.pEdit, e.pEdit)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectFieldPerm): String {
        val now = LocalDateTime.now()
        val session =  sessionService.getSession()
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
            set(it.fieldId, e.fieldId)
            set(it.pRead, e.pRead)
            set(it.pEdit, e.pEdit)
            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
        } as String
    }

    fun selectAll(): MutableList<BizObjectFieldPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectFieldPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .where { table.id eq id }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<BizObjectFieldPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
                table.fieldId eq fieldId
            }.mapTo(ArrayList()) { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<BizObjectFieldPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByFieldId(
        fieldId: String,
    ): MutableList<BizObjectFieldPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.fieldId eq fieldId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun batchInsert(es: List<BizObjectFieldPerm>): MutableList<Int> {
        val now = LocalDateTime.now()
        val session =  sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().batchInsert(table) {
            es.forEach { e ->
                item {
                    e.createDate = now
                    e.modifyDate = now
                    e.createById = accountId
                    e.modifyById = accountId
                    e.createBy = accountName
                    e.modifyBy = accountName

                    set(it.roleId, e.roleId)
                    set(it.fieldId, e.fieldId)
                    set(it.pRead, e.pRead)
                    set(it.pEdit, e.pEdit)
                    set(it.createDate, e.createBy)
                    set(it.modifyDate, e.modifyDate)
                    set(it.createById, e.createById)
                    set(it.modifyById, e.modifyById)
                    set(it.createBy, e.createBy)
                    set(it.modifyBy, e.modifyBy)
                }
            }
        }.toMutableList()
    }

    fun batchInsertOrUpdate(es: List<BizObjectFieldPerm>): MutableList<Int> {
        dbService.db().useTransaction {
            return es.mapTo(ArrayList()) { insertOrUpdate(it) }
        }
    }

    fun insertOrUpdate(e: BizObjectFieldPerm): Int {
        val now = LocalDateTime.now()
        val session =  sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        e.createDate = now
        e.modifyDate = now
        e.createById = accountId
        e.modifyById = accountId
        e.createBy = accountName
        e.modifyBy = accountName
        return dbService.db().insertOrUpdate(table) {
            set(it.roleId, e.roleId)
            set(it.fieldId, e.fieldId)
            set(it.pRead, e.pRead)
            set(it.pEdit, e.pEdit)
            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
            onDuplicateKey(table.roleId, table.fieldId) {
                set(it.pRead, e.pRead)
                set(it.pEdit, e.pEdit)
                set(it.modifyDate, e.modifyDate)
                set(it.modifyById, e.modifyById)
                set(it.modifyBy, e.modifyBy)
            }
        }
    }


}