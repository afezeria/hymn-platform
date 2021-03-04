package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.MenuItemPerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class MenuItemPermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(value = "菜单项id ;;fk:[core_custom_menu_item cascade];idx", required = true)
    var menuItemId: String,
    @ApiModelProperty(value = "是否可见")
    var visible: Boolean = false,
) {
    @ApiModelProperty(value = "角色名称", notes = "只用于后端返回数据")
    var roleName: String? = null

    @ApiModelProperty(value = "菜单项名称", notes = "只用于后端返回数据")
    var menuItemName: String? = null

    @ApiModelProperty(value = "菜单项唯一标识", notes = "只用于后端返回数据")
    var menuItemApi: String? = null

    fun toEntity(): MenuItemPerm {
        return MenuItemPerm(
            roleId = roleId,
            menuItemId = menuItemId,
            visible = visible,
        )
    }

    fun update(entity: MenuItemPerm) {
        entity.also {
            it.roleId = roleId
            it.menuItemId = menuItemId
            it.visible = visible
        }
    }
}
