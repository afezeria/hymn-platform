package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectPerm
import github.afezeria.hymn.core.module.table.CoreBizObjectPerms
import github.afezeria.hymn.core.module.table.CoreBizObjects
import github.afezeria.hymn.core.module.table.CoreRoles
import github.afezeria.hymn.core.module.view.BizObjectPermListView
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectPermDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectPerm, CoreBizObjectPerms>(
    table = CoreBizObjectPerms(),
    databaseService = databaseService
) {

    private val bizObjects = CoreBizObjects()
    private val roles = CoreRoles()

    fun selectByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm? {
        return singleRowSelect(listOf(table.roleId eq roleId, table.bizObjectId eq bizObjectId))
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<BizObjectPerm> {
        return select({ table.roleId eq roleId })
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectPerm> {
        return select({ table.bizObjectId eq bizObjectId })
    }

    val dtoColumns = listOf(
        table.roleId,
        table.bizObjectId,
        table.ins,
        table.upd,
        table.del,
        table.que,
        table.queryWithAccountTree,
        table.queryWithOrg,
        table.queryWithOrgTree,
        table.queryAll,
        table.editAll,
        roles.name,
        bizObjects.name
    )

    fun selectView(
        whereExpr: (CoreBizObjectPerms) -> ColumnDeclaring<Boolean>
    ): MutableList<BizObjectPermListView> {
        return table.run {
            databaseService.db().from(this)
                .innerJoin(bizObjects, bizObjects.id eq bizObjectId)
                .innerJoin(roles, roles.id eq roleId)
                .select(dtoColumns)
                .where {
                    (bizObjects.active eq true) and
                        whereExpr(this)
                }
                .mapTo(ArrayList()) {
                    BizObjectPermListView(
                        roleId = requireNotNull(it[roleId]),
                        bizObjectId = requireNotNull(it[bizObjectId]),
                        roleName = requireNotNull(it[roles.name]),
                        bizObjectName = requireNotNull(it[bizObjects.name]),
                        ins = requireNotNull(it[ins]),
                        upd = requireNotNull(it[upd]),
                        del = requireNotNull(it[del]),
                        que = requireNotNull(it[que]),
                        queryWithAccountTree = requireNotNull(it[queryWithAccountTree]),
                        queryWithOrg = requireNotNull(it[queryWithOrg]),
                        queryWithOrgTree = requireNotNull(it[queryWithOrgTree]),
                        queryAll = requireNotNull(it[queryAll]),
                        editAll = requireNotNull(it[editAll]),
                    )
                }
        }
    }

}