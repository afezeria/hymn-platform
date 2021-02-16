package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectMappingItems(alias: String? = null) :
    AbstractTable<BizObjectMappingItem>(
        "core_biz_object_mapping_item",
        schema = "hymn",
        alias = alias
    ) {

    val mappingId = varchar("mapping_id")
    val sourceFieldId = varchar("source_field_id")
    val targetFieldId = varchar("target_field_id")
    val refField1Id = varchar("ref_field1_id")
    val refField1BizObjectId = varchar("ref_field1_biz_object_id")
    val refField2Id = varchar("ref_field2_id")
    val refField2BizObjectId = varchar("ref_field2_biz_object_id")
    val refField3Id = varchar("ref_field3_id")
    val refField3BizObjectId = varchar("ref_field3_biz_object_id")
    val refField4Id = varchar("ref_field4_id")
    val refField4BizObjectId = varchar("ref_field4_biz_object_id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
