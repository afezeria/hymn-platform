package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.BizObjectMapping
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectMappings(alias: String? = null) :
    AbstractTable<BizObjectMapping>("core_biz_object_mapping", schema = "hymn", alias = alias) {

    val sourceBizObjectId = varchar("source_biz_object_id")
    val sourceTypeId = varchar("source_type_id")
    val targetBizObjectId = varchar("target_biz_object_id")
    val targetTypeId = varchar("target_type_id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
