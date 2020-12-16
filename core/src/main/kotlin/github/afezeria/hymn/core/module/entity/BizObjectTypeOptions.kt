package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 业务对象记录类型可选项限制
 * 限制指定记录类型时指定字段 （多选/单选）的可用选项
 * @author afezeria
 */
@ApiModel(value="业务对象记录类型可选项限制",description = """业务对象记录类型可选项限制
限制指定记录类型时指定字段 （多选/单选）的可用选项""")
data class BizObjectTypeOptions(

    @ApiModelProperty(value = "所属对象 ;;idx")
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型id ;;fk:[core_biz_object_type cascade];idx")
    var typeId: String,
    @ApiModelProperty(value = "字段id ;;fk:[core_biz_object_field cascade]")
    var fieldId: String,
    @ApiModelProperty(value = "字段关联的字典项id ;;fk:[core_dict_item cascade]")
    var dictItemId: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
