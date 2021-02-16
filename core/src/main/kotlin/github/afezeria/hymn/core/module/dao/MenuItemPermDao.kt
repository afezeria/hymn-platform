package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.MenuItemPerm
import github.afezeria.hymn.core.module.table.CoreMenuItemPerms
import org.ktorm.dsl.eq
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



}