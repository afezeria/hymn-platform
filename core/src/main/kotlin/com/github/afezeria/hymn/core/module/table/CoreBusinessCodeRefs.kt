package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.BusinessCodeRef
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBusinessCodeRefs(alias: String? = null) :
    AbstractTable<BusinessCodeRef>("core_business_code_ref", schema = "hymn", alias = alias) {

    val byTriggerId = varchar("by_trigger_id")
    val byInterfaceId = varchar("by_interface_id")
    val byCustomFunctionId = varchar("by_custom_function_id")
    val bizObjectId = varchar("biz_object_id")
    val fieldId = varchar("field_id")
    val customFunctionId = varchar("custom_function_id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
