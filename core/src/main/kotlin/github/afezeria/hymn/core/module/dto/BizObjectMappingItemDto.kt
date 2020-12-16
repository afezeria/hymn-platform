package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class BizObjectMappingItemDto(
    @ApiModelProperty(value = "")
    var mappingId: String,
    @ApiModelProperty(value = "源字段id，如果直接从源字段映射到目标字段则 ref_field 和 ref_field_biz_object_id 都为空 ;;fk:[core_biz_object_field cascade]")
    var sourceFieldId: String,
    @ApiModelProperty(value = "目标字段id ;;fk:[core_biz_object_field cascade]")
    var targetFieldId: String,
    @ApiModelProperty(value = "引用字段1 ;;fk:[core_biz_object_field cascade]", required = true)
    var refField1Id: String? = null,
    @ApiModelProperty(value = "ref_field1_id 表示的字段所属的对象id，也是source_field_id关联的对象的id ;;fk:[core_biz_object cascade]", required = true)
    var refField1BizObjectId: String? = null,
    @ApiModelProperty(value = "引用字段2 ;;fk:[core_biz_object_field cascade]", required = true)
    var refField2Id: String? = null,
    @ApiModelProperty(value = "ref_field2_id 表示的字段所属的对象id，也是 ref_field1_id 关联的对象的id ;;fk:[core_biz_object cascade]", required = true)
    var refField2BizObjectId: String? = null,
    @ApiModelProperty(value = "引用字段3 ;;fk:[core_biz_object_field cascade]", required = true)
    var refField3Id: String? = null,
    @ApiModelProperty(value = "ref_field3_id 表示的字段所属的对象id，也是 ref_field2_id 关联的对象的id ;;fk:[core_biz_object cascade]", required = true)
    var refField3BizObjectId: String? = null,
    @ApiModelProperty(value = "引用字段4 ;;fk:[core_biz_object_field cascade]", required = true)
    var refField4Id: String? = null,
    @ApiModelProperty(value = "ref_field4_api 表示的字段所属的对象api，也是 ref_field3_api 关联的对象的api ;;fk:[core_biz_object cascade]", required = true)
    var refField4BizObjectId: String? = null,
){
    fun toEntity(): BizObjectMappingItem {
        return BizObjectMappingItem(
            mappingId = mappingId,
            sourceFieldId = sourceFieldId,
            targetFieldId = targetFieldId,
            refField1Id = refField1Id,
            refField1BizObjectId = refField1BizObjectId,
            refField2Id = refField2Id,
            refField2BizObjectId = refField2BizObjectId,
            refField3Id = refField3Id,
            refField3BizObjectId = refField3BizObjectId,
            refField4Id = refField4Id,
            refField4BizObjectId = refField4BizObjectId,
        )
    }

    fun fromEntity(entity: BizObjectMappingItem): BizObjectMappingItemDto {
        return entity.run {
            BizObjectMappingItemDto(
                mappingId = mappingId,
                sourceFieldId = sourceFieldId,
                targetFieldId = targetFieldId,
                refField1Id = refField1Id,
                refField1BizObjectId = refField1BizObjectId,
                refField2Id = refField2Id,
                refField2BizObjectId = refField2BizObjectId,
                refField3Id = refField3Id,
                refField3BizObjectId = refField3BizObjectId,
                refField4Id = refField4Id,
                refField4BizObjectId = refField4BizObjectId,
          )
        }
    }

    fun update(entity: BizObjectMappingItem) {
        entity.also {
            it.mappingId = mappingId
            it.sourceFieldId = sourceFieldId
            it.targetFieldId = targetFieldId
            it.refField1Id = refField1Id
            it.refField1BizObjectId = refField1BizObjectId
            it.refField2Id = refField2Id
            it.refField2BizObjectId = refField2BizObjectId
            it.refField3Id = refField3Id
            it.refField3BizObjectId = refField3BizObjectId
            it.refField4Id = refField4Id
            it.refField4BizObjectId = refField4BizObjectId
        }
    }
}
