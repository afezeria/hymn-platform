package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectFieldPerms(alias: String? = null) :
    AbstractTable<BizObjectFieldPerm>(
        "core_biz_object_field_perm",
        schema = "hymn",
        alias = alias
    ) {

    val roleId = varchar("role_id")
    val bizObjectId = varchar("biz_object_id")
    val fieldId = varchar("field_id")
    val pRead = boolean("p_read")
    val pEdit = boolean("p_edit")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
