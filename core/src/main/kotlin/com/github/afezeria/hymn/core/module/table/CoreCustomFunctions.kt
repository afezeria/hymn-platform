package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.CustomFunction
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCustomFunctions(alias: String? = null) :
    AbstractTable<CustomFunction>("core_custom_function", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val type = varchar("type")
    val code = varchar("code")
    val baseFun = boolean("base_fun")
    val lang = varchar("lang")
    val optionText = varchar("option_text")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
