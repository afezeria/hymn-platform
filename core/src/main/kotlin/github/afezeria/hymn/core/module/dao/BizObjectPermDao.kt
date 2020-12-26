package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.entity.BizObjectPerm
import github.afezeria.hymn.core.module.table.CoreBizObjectPerms
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertOrUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@Component
class BizObjectPermDao {

    @Autowired
    private lateinit var dbService: DataBaseService

    @Autowired
    private lateinit var sessionService: SessionService

    val table = CoreBizObjectPerms()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: BizObjectPerm): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.roleId, e.roleId)
            set(it.bizObjectId, e.bizObjectId)
            set(it.ins, e.ins)
            set(it.upd, e.upd)
            set(it.del, e.del)
            set(it.que, e.que)
            set(it.queryWithAccountTree, e.queryWithAccountTree)
            set(it.queryWithDept, e.queryWithDept)
            set(it.queryWithDeptTree, e.queryWithDeptTree)
            set(it.queryAll, e.queryAll)
            set(it.editAll, e.editAll)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectPerm): String {
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
            set(it.ins, e.ins)
            set(it.upd, e.upd)
            set(it.del, e.del)
            set(it.que, e.que)
            set(it.queryWithAccountTree, e.queryWithAccountTree)
            set(it.queryWithDept, e.queryWithDept)
            set(it.queryWithDeptTree, e.queryWithDeptTree)
            set(it.queryAll, e.queryAll)
            set(it.editAll, e.editAll)
            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
        } as String
    }

    fun batchInsert(es: List<BizObjectPerm>): MutableList<Int> {
        val now = LocalDateTime.now()
        val session = sessionService.getSession()
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
                    set(it.bizObjectId, e.bizObjectId)
                    set(it.ins, e.ins)
                    set(it.upd, e.upd)
                    set(it.del, e.del)
                    set(it.que, e.que)
                    set(it.queryWithAccountTree, e.queryWithAccountTree)
                    set(it.queryWithDept, e.queryWithDept)
                    set(it.queryWithDeptTree, e.queryWithDeptTree)
                    set(it.queryAll, e.queryAll)
                    set(it.editAll, e.editAll)
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

    fun batchInsertOrUpdate(es: List<BizObjectPerm>): MutableList<Int> {
        dbService.db().useTransaction {
            return es.mapTo(ArrayList()) { insertOrUpdate(it) }
        }
    }

    fun insertOrUpdate(e: BizObjectPerm): Int {
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
        return dbService.db().insertOrUpdate(table) {
            set(it.roleId, e.roleId)
            set(it.bizObjectId, e.bizObjectId)
            set(it.ins, e.ins)
            set(it.upd, e.upd)
            set(it.del, e.del)
            set(it.que, e.que)
            set(it.queryWithAccountTree, e.queryWithAccountTree)
            set(it.queryWithDept, e.queryWithDept)
            set(it.queryWithDeptTree, e.queryWithDeptTree)
            set(it.queryAll, e.queryAll)
            set(it.editAll, e.editAll)
            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
            onDuplicateKey(table.roleId, table.bizObjectId) {
                set(it.ins, e.ins)
                set(it.upd, e.upd)
                set(it.del, e.del)
                set(it.que, e.que)
                set(it.queryWithAccountTree, e.queryWithAccountTree)
                set(it.queryWithDept, e.queryWithDept)
                set(it.queryWithDeptTree, e.queryWithDeptTree)
                set(it.queryAll, e.queryAll)
                set(it.editAll, e.editAll)
                set(it.modifyDate, e.modifyDate)
                set(it.modifyById, e.modifyById)
                set(it.modifyBy, e.modifyBy)
            }
        }
    }

    fun selectAll(): MutableList<BizObjectPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<BizObjectPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
                table.bizObjectId eq bizObjectId
            }.mapTo(ArrayList()) { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<BizObjectPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }


}