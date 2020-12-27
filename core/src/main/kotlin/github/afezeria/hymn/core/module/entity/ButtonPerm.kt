package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 按钮权限 ;;uk:[[role_id button_id]]
 * @author afezeria
 */
@ApiModel(value = "按钮权限", description = """按钮权限 ;;uk:[[role_id button_id]]""")
data class ButtonPerm(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(value = "按钮id ;;fk:[core_custom_button cascade];idx", required = true)
    var buttonId: String,
    @ApiModelProperty(value = "是否可见", required = true)
    var visible: Boolean,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
