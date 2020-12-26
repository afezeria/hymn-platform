package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectMappingItems(alias: String? = null) :
    BaseTable<BizObjectMappingItem>(
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
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectMappingItem(
        mappingId = requireNotNull(row[this.mappingId]) { "field BizObjectMappingItem.mappingId should not be null" },
        sourceFieldId = requireNotNull(row[this.sourceFieldId]) { "field BizObjectMappingItem.sourceFieldId should not be null" },
        targetFieldId = requireNotNull(row[this.targetFieldId]) { "field BizObjectMappingItem.targetFieldId should not be null" },
        refField1Id = row[this.refField1Id],
        refField1BizObjectId = row[this.refField1BizObjectId],
        refField2Id = row[this.refField2Id],
        refField2BizObjectId = row[this.refField2BizObjectId],
        refField3Id = row[this.refField3Id],
        refField3BizObjectId = row[this.refField3BizObjectId],
        refField4Id = row[this.refField4Id],
        refField4BizObjectId = row[this.refField4BizObjectId],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectMappingItem.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field BizObjectMappingItem.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field BizObjectMappingItem.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field BizObjectMappingItem.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field BizObjectMappingItem.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field BizObjectMappingItem.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field BizObjectMappingItem.modifyDate should not be null" }
    }
}
