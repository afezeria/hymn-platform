package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class ButtonPermListView(
    @ApiModelProperty(value = "角色id")
    var roleId: String,
    @ApiModelProperty(value = "角色名称")
    var roleName: String,
    @ApiModelProperty(value = "按钮id")
    var buttonId: String,
    @ApiModelProperty(value = "按钮名称")
    var buttonName: String,
    @ApiModelProperty(value = "是否可见")
    var visible: Boolean = false,
    @ApiModelProperty(value = "对象名称")
    var bizObjectName: String? = null,
    @ApiModelProperty(value = "对象id")
    var bizObjectId: String? = null,
)