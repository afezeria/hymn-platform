package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.entity.Role
import github.afezeria.hymn.core.module.table.CoreRoles
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@Component
class RoleDao {

    @Autowired
    private lateinit var dbService: DatabaseService

    @Autowired
    private lateinit var  sessionService: SessionService

    val table = CoreRoles()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: Role): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session =  sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.name, e.name)
            set(it.remark, e.remark)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: Role): String {
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
            set(it.name, e.name)
            set(it.remark, e.remark)
            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
        } as String
    }

    fun selectAll(): MutableList<Role> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): Role? {
        return dbService.db().from(table)
            .select(table.columns)
            .where { table.id eq id }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<Role> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectIds(ids: List<String>?): MutableList<String> {
        val select = dbService.db().from(table)
            .select(table.id)
        val query = if (ids == null) select else select.where { table.id inList ids }
        return query.mapTo(ArrayList()) { it[table.id]!! }
    }


}