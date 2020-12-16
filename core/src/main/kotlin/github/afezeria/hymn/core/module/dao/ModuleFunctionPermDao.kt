package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import github.afezeria.hymn.core.module.table.CoreModuleFunctionPerms
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
class ModuleFunctionPermDao(
    private val dbService: DataBaseService,
    private val sessionService:SessionService,
) {

    val table = CoreModuleFunctionPerms()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: ModuleFunctionPerm): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.roleId, e.roleId)
            set(it.moduleFunctionId, e.moduleFunctionId)
            set(it.perm, e.perm)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: ModuleFunctionPerm): String {
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
            set(it.moduleFunctionId, e.moduleFunctionId)
            set(it.perm, e.perm)
        } as String
    }

    fun selectAll(): List<ModuleFunctionPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }

    fun selectById(id: String): ModuleFunctionPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleIdAndModuleFunctionId(
        roleId: String,
        moduleFunctionId: String,
    ): ModuleFunctionPerm? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
                table.moduleFunctionId eq moduleFunctionId
            }.map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByRoleId(
        roleId: String,
    ): List<ModuleFunctionPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.roleId eq roleId
            }.map { table.createEntity(it) }
    }

    fun selectByModuleFunctionId(
        moduleFunctionId: String,
    ): List<ModuleFunctionPerm> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.moduleFunctionId eq moduleFunctionId
            }.map { table.createEntity(it) }
    }


}