package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 业务对象记录类型可选项限制
 * 限制指定记录类型时指定字段 （多选/单选）的可用选项
 * @author afezeria
 */
@ApiModel(
    value = "业务对象记录类型可选项限制", description = """业务对象记录类型可选项限制
限制指定记录类型时指定字段 （多选/单选）的可用选项"""
)
data class BizObjectTypeOptions(

    @ApiModelProperty(value = "所属对象 ;;idx", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型id ;;fk:[core_biz_object_type cascade];idx", required = true)
    var typeId: String,
    @ApiModelProperty(value = "字段id ;;fk:[core_biz_object_field cascade]", required = true)
    var fieldId: String,
    @ApiModelProperty(value = "字段关联的字典项id ;;fk:[core_dict_item cascade]", required = true)
    var dictItemId: String,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
