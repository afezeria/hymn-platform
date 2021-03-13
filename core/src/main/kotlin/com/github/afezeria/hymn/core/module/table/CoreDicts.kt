package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.Dict
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreDicts(alias: String? = null) :
    AbstractTable<Dict>("core_dict", schema = "hymn", alias = alias) {

    val fieldId = varchar("field_id")
    val parentDictId = varchar("parent_dict_id")
    val name = varchar("name")
    val api = varchar("api")
    val remark = varchar("remark")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
