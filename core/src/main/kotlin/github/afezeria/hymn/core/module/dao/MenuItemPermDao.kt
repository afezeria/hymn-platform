package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.entity.MenuItemPerm
import github.afezeria.hymn.core.module.table.CoreMenuItemPerms
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
class MenuItemPermDao {

    @Autowired
    private lateinit var dbService: DataBaseService

    @Autowired
    private lateinit var sessionService: SessionService

    val table = CoreMenuItemPerms()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: MenuItemPerm): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.roleId, e.roleId)
            set(it.menuItemId, e.menuItemId)
            set(it.visible, e.visible)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: MenuItemPerm): String {
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
            set(it.menuItemId, e.menuItemId)
            set(it.visible, e.visible)
            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
        } as String
    }

    fun selectAll(): MutableList<MenuItemPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): MenuItemPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<MenuItemPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<MenuItemPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByMenuItemId(
        menuItemId: String,
    ): MutableList<MenuItemPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.menuItemId eq menuItemId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }


    fun batchInsert(es: List<MenuItemPerm>): MutableList<Int> {
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
                    set(it.menuItemId, e.menuItemId)
                    set(it.visible, e.visible)
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

    fun batchInsertOrUpdate(es: List<MenuItemPerm>): MutableList<Int> {
        dbService.db().useTransaction {
            return es.mapTo(ArrayList()) { insertOrUpdate(it) }
        }
    }

    fun insertOrUpdate(e: MenuItemPerm): Int {
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
            set(it.menuItemId, e.menuItemId)
            set(it.visible, e.visible)

            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
            onDuplicateKey(table.roleId, table.menuItemId) {
                set(it.visible, e.visible)
                set(it.modifyDate, e.modifyDate)
                set(it.modifyById, e.modifyById)
                set(it.modifyBy, e.modifyBy)
            }
        }
    }

}