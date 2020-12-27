package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 业务对象记录类型、角色和页面布局关联表 ;;uk:[[role_id type_id layout_id]]
 * @author afezeria
 */
@ApiModel(
    value = "业务对象记录类型、角色和页面布局关联表",
    description = """业务对象记录类型、角色和页面布局关联表 ;;uk:[[role_id type_id layout_id]]"""
)
data class BizObjectTypeLayout(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(value = "业务对象id ;;fk:[core_biz_object cascade];idx", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型id ;;fk:[core_biz_object_type cascade]", required = true)
    var typeId: String,
    @ApiModelProperty(value = "页面布局id ;;fk:[core_biz_object_layout cascade]", required = true)
    var layoutId: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
