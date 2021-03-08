package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypeLayoutListView(
    @ApiModelProperty(value = "角色id")
    var roleId: String,
    @ApiModelProperty(value = "记录类型id")
    var typeId: String,
    @ApiModelProperty(value = "页面布局id")
    var layoutId: String,
    @ApiModelProperty(value = "角色名称")
    var roleName: String,
    @ApiModelProperty(value = "类型名称")
    var typeName: String,
    @ApiModelProperty(value = "布局名称")
    var layoutName: String,
)