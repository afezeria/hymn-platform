package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 业务对象记录类型、角色和页面布局关联表 ;;uk:[[role_id type_id layout_id]]
 * @author afezeria
 */
@ApiModel(value="业务对象记录类型、角色和页面布局关联表",description = """业务对象记录类型、角色和页面布局关联表 ;;uk:[[role_id type_id layout_id]]""")
data class BizObjectTypeLayout(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "业务对象id ;;fk:[core_biz_object cascade];idx")
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型id ;;fk:[core_biz_object_type cascade]")
    var typeId: String,
    @ApiModelProperty(value = "页面布局id ;;fk:[core_biz_object_layout cascade]")
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
