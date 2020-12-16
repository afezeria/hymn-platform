package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 菜单项权限
 * @author afezeria
 */
@ApiModel(value="菜单项权限",description = """菜单项权限""")
data class MenuItemPerm(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "菜单项id ;;fk:[core_custom_menu_item cascade];idx")
    var menuItemId: String,
    @ApiModelProperty(value = "")
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
