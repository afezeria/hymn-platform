package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.CustomMenuItem
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCustomMenuItems(alias: String? = null) :
    BaseTable<CustomMenuItem>("core_custom_menu_item", schema = "hymn", alias = alias) {

    val name = varchar("name")
    val path = varchar("path")
    val pathType = varchar("path_type")
    val action = varchar("action")
    val clientType = varchar("client_type")
    val icon = varchar("icon")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = CustomMenuItem(
        name = requireNotNull(row[this.name]) { "field CustomMenuItem.name should not be null" },
        path = requireNotNull(row[this.path]) { "field CustomMenuItem.path should not be null" },
        pathType = requireNotNull(row[this.pathType]) { "field CustomMenuItem.pathType should not be null" },
        action = requireNotNull(row[this.action]) { "field CustomMenuItem.action should not be null" },
        clientType = requireNotNull(row[this.clientType]) { "field CustomMenuItem.clientType should not be null" },
        icon = requireNotNull(row[this.icon]) { "field CustomMenuItem.icon should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field CustomMenuItem.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field CustomMenuItem.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field CustomMenuItem.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field CustomMenuItem.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field CustomMenuItem.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field CustomMenuItem.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field CustomMenuItem.modifyDate should not be null" }
    }
}
