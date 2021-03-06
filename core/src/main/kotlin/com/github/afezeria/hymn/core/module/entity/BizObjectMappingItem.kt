package com.github.afezeria.hymn.core.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity

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

    @ApiModelProperty(value = "对象映射关系表id ")
    var mappingId: String,
    @ApiModelProperty(
        value = "源字段id，如果直接从源字段映射到目标字段则 ref_field 和 ref_field_biz_object_id 都为空 "
    )
    var sourceFieldId: String,
    @ApiModelProperty(value = "目标字段id ")
    var targetFieldId: String,
    @ApiModelProperty(value = "引用字段1 ")
    var refField1Id: String? = null,
    @ApiModelProperty(value = "ref_field1_id 表示的字段所属的对象id，也是source_field_id关联的对象的id ")
    var refField1BizObjectId: String? = null,
    @ApiModelProperty(value = "引用字段2 ")
    var refField2Id: String? = null,
    @ApiModelProperty(value = "ref_field2_id 表示的字段所属的对象id，也是 ref_field1_id 关联的对象的id ")
    var refField2BizObjectId: String? = null,
    @ApiModelProperty(value = "引用字段3 ")
    var refField3Id: String? = null,
    @ApiModelProperty(value = "ref_field3_id 表示的字段所属的对象id，也是 ref_field2_id 关联的对象的id ")
    var refField3BizObjectId: String? = null,
    @ApiModelProperty(value = "引用字段4 ")
    var refField4Id: String? = null,
    @ApiModelProperty(value = "ref_field4_api 表示的字段所属的对象api，也是 ref_field3_api 关联的对象的api ")
    var refField4BizObjectId: String? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
