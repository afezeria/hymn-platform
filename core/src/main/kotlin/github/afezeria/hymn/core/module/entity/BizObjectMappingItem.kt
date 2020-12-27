package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 对象映射关系表明细 描述映射规则
 * @author afezeria
 */
@ApiModel(value = "对象映射关系表明细", description = """对象映射关系表明细 描述映射规则""")
data class BizObjectMappingItem(

    @ApiModelProperty(value = "对象映射关系表id ;;fk:[core_biz_object_mapping cascade]", required = true)
    var mappingId: String,
    @ApiModelProperty(
        value = "源字段id，如果直接从源字段映射到目标字段则 ref_field 和 ref_field_biz_object_id 都为空 ;;fk:[core_biz_object_field cascade]",
        required = true
    )
    var sourceFieldId: String,
    @ApiModelProperty(value = "目标字段id ;;fk:[core_biz_object_field cascade]", required = true)
    var targetFieldId: String,
    @ApiModelProperty(value = "引用字段1 ;;fk:[core_biz_object_field cascade]")
    var refField1Id: String? = null,
    @ApiModelProperty(value = "ref_field1_id 表示的字段所属的对象id，也是source_field_id关联的对象的id ;;fk:[core_biz_object cascade]")
    var refField1BizObjectId: String? = null,
    @ApiModelProperty(value = "引用字段2 ;;fk:[core_biz_object_field cascade]")
    var refField2Id: String? = null,
    @ApiModelProperty(value = "ref_field2_id 表示的字段所属的对象id，也是 ref_field1_id 关联的对象的id ;;fk:[core_biz_object cascade]")
    var refField2BizObjectId: String? = null,
    @ApiModelProperty(value = "引用字段3 ;;fk:[core_biz_object_field cascade]")
    var refField3Id: String? = null,
    @ApiModelProperty(value = "ref_field3_id 表示的字段所属的对象id，也是 ref_field2_id 关联的对象的id ;;fk:[core_biz_object cascade]")
    var refField3BizObjectId: String? = null,
    @ApiModelProperty(value = "引用字段4 ;;fk:[core_biz_object_field cascade]")
    var refField4Id: String? = null,
    @ApiModelProperty(value = "ref_field4_api 表示的字段所属的对象api，也是 ref_field3_api 关联的对象的api ;;fk:[core_biz_object cascade]")
    var refField4BizObjectId: String? = null,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
