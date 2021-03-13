package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.CustomInterface
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCustomInterfaces(alias: String? = null) :
    AbstractTable<CustomInterface>("core_custom_interface", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val name = varchar("name")
    val code = varchar("code")
    val active = boolean("active")
    val lang = varchar("lang")
    val optionText = varchar("option_text")
    val remark = varchar("remark")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
