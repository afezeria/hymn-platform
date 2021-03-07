package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.MenuItemPerm
import github.afezeria.hymn.core.module.table.CoreCustomMenuItems
import github.afezeria.hymn.core.module.table.CoreMenuItemPerms
import github.afezeria.hymn.core.module.table.CoreRoles
import github.afezeria.hymn.core.module.view.MenuItemPermListView
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class MenuItemPermDao(
    databaseService: DatabaseService
) : AbstractDao<MenuItemPerm, CoreMenuItemPerms>(
    table = CoreMenuItemPerms(),
    databaseService = databaseService
) {


    fun selectByRoleId(
        roleId: String,
    ): MutableList<MenuItemPerm> {
        return select({ it.roleId eq roleId })
    }

    fun selectByMenuItemId(
        menuItemId: String,
    ): MutableList<MenuItemPerm> {
        return select({ it.menuItemId eq menuItemId })
    }

    private val menuItems = CoreCustomMenuItems()
    private val roles = CoreRoles()

    fun selectView(whereExpr: (CoreMenuItemPerms) -> ColumnDeclaring<Boolean>): MutableList<MenuItemPermListView> {
        return table.run {
            databaseService.db().from(this)
                .innerJoin(menuItems, menuItems.id eq menuItemId)
                .innerJoin(roles, roles.id eq roleId)
                .select(
                    roleId,
                    menuItemId,
                    visible,
                    roles.name,
                    menuItems.name,
                    menuItems.api,
                )
                .where {
                    whereExpr(this)
                }
                .mapTo(ArrayList()) {
                    MenuItemPermListView(
                        roleId = requireNotNull(it[roleId]),
                        roleName = requireNotNull(it[roles.name]),
                        menuItemId = requireNotNull(it[menuItemId]),
                        menuItemName = requireNotNull(it[menuItems.name]),
                        menuItemApi = requireNotNull(it[menuItems.api]),
                        visible = requireNotNull(it[visible]),
                    )
                }
        }

    }


}