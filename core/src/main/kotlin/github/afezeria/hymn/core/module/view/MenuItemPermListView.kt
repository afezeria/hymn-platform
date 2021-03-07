package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class MenuItemPermListView(
    @ApiModelProperty(value = "角色id")
    var roleId: String,
    @ApiModelProperty(value = "角色名称", notes = "只用于后端返回数据")
    var roleName: String,
    @ApiModelProperty(value = "菜单项id")
    var menuItemId: String,
    @ApiModelProperty(value = "菜单项名称", notes = "只用于后端返回数据")
    var menuItemName: String,
    @ApiModelProperty(value = "菜单项唯一标识", notes = "只用于后端返回数据")
    var menuItemApi: String,
    @ApiModelProperty(value = "是否可见")
    var visible: Boolean = false,
)