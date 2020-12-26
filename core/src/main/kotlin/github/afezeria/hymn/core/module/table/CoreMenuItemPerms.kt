package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.MenuItemPerm
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreMenuItemPerms(alias: String? = null) :
    BaseTable<MenuItemPerm>("core_menu_item_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val menuItemId = varchar("menu_item_id")
    val visible = boolean("visible")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = MenuItemPerm(
        roleId = requireNotNull(row[this.roleId]) { "field MenuItemPerm.roleId should not be null" },
        menuItemId = requireNotNull(row[this.menuItemId]) { "field MenuItemPerm.menuItemId should not be null" },
        visible = requireNotNull(row[this.visible]) { "field MenuItemPerm.visible should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field MenuItemPerm.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field MenuItemPerm.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field MenuItemPerm.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field MenuItemPerm.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field MenuItemPerm.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field MenuItemPerm.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field MenuItemPerm.modifyDate should not be null" }
    }
}
