package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.CustomMenuGroup
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCustomMenuGroup(alias: String? = null) :
    AbstractTable<CustomMenuGroup>("core_custom_menu_group", schema = "hymn", alias = alias) {

    val name = varchar("name")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
