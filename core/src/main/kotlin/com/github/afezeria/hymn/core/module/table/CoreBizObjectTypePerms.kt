package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectTypePerms(alias: String? = null) :
    AbstractTable<BizObjectTypePerm>("core_biz_object_type_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val typeId = varchar("type_id")
    val visible = boolean("visible")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
