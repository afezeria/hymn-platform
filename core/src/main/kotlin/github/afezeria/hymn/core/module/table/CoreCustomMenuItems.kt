package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.CustomMenuItem
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCustomMenuItems(alias: String? = null) :
    AbstractTable<CustomMenuItem>("core_custom_menu_item", schema = "hymn", alias = alias) {

    val name = varchar("name")
    val path = varchar("path")
    val pathType = varchar("path_type")
    val action = varchar("action")
    val clientType = varchar("client_type")
    val icon = varchar("icon")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
