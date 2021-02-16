package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.MenuItemPerm
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreMenuItemPerms(alias: String? = null) :
    AbstractTable<MenuItemPerm>("core_menu_item_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val menuItemId = varchar("menu_item_id")
    val visible = boolean("visible")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
