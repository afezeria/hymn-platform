package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.BizObjectTypeFieldOption
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectTypeFieldOptions(alias: String? = null) :
    AbstractTable<BizObjectTypeFieldOption>(
        "core_biz_object_type_field_option",
        schema = "hymn",
        alias = alias
    ) {

    val bizObjectId = varchar("biz_object_id")
    val typeId = varchar("type_id")
    val fieldId = varchar("field_id")
    val dictItemId = varchar("dict_item_id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
