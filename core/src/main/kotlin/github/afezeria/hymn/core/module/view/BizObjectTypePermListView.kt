package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypePermListView(
    @ApiModelProperty(value = "角色id")
    var roleId: String,
    @ApiModelProperty(value = "角色名称")
    var roleName: String,
    @ApiModelProperty(value = "类型id")
    var typeId: String,
    @ApiModelProperty(value = "类型名称")
    var typeName: String,
    @ApiModelProperty(value = "类型所属对象id")
    var bizObjectId: String,
    @ApiModelProperty(value = "创建数据时选择特定记录类型的权限")
    var visible: Boolean = false,
)