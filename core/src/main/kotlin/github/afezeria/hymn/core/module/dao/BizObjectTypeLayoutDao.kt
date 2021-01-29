package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout
import github.afezeria.hymn.core.module.table.CoreBizObjectTypeLayouts
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
@Component
class BizObjectTypeLayoutDao {

    @Autowired
    private lateinit var dbService: DatabaseService

    @Autowired
    private lateinit var  sessionService: SessionService

    val table = CoreBizObjectTypeLayouts()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

   fun update(e: BizObjectTypeLayout): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        e.modifyDate = LocalDateTime.now()
        e.modifyById = session.accountId
        e.modifyBy = session.accountName
        return  dbService.db().update(table) {
            set(it.roleId, e.roleId)
            set(it.bizObjectId, e.bizObjectId)
            set(it.typeId, e.typeId)
            set(it.layoutId, e.layoutId)
            set(it.modifyById, e.modifyById)
            set(it.modifyBy, e.modifyBy)
            set(it.modifyDate, e.modifyDate)
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: BizObjectTypeLayout): String {
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
            set(it.bizObjectId, e.bizObjectId)
            set(it.typeId, e.typeId)
            set(it.layoutId, e.layoutId)
            set(it.createDate, e.createDate)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
        } as String
    }

    fun selectAll(): MutableList<BizObjectTypeLayout> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): BizObjectTypeLayout? {
        return dbService.db().from(table)
            .select(table.columns)
            .where { table.id eq id }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<BizObjectTypeLayout> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByRoleIdAndTypeIdAndLayoutId(
        roleId: String,
        typeId: String,
        layoutId: String,
    ): BizObjectTypeLayout? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
                table.typeId eq typeId
                table.layoutId eq layoutId
            }.mapTo(ArrayList()) { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<BizObjectTypeLayout> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectTypeLayout> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bizObjectId eq bizObjectId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun batchInsert(es: List<BizObjectTypeLayout>): MutableList<Int> {
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
                    set(it.bizObjectId, e.bizObjectId)
                    set(it.typeId, e.typeId)
                    set(it.layoutId, e.layoutId)
                    set(it.createDate, e.createDate)
                    set(it.modifyDate, e.modifyDate)
                    set(it.createById, e.createById)
                    set(it.modifyById, e.modifyById)
                    set(it.createBy, e.createBy)
                    set(it.modifyBy, e.modifyBy)
                }
            }
        }.toMutableList()
    }


}