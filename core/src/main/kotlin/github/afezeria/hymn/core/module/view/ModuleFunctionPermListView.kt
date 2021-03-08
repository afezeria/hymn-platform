package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class ModuleFunctionPermListView(
    @ApiModelProperty(value = "角色id")
    var roleId: String,
    @ApiModelProperty(value = "角色名称")
    var roleName: String,
    @ApiModelProperty(value = "功能api")
    var functionApi: String,
    @ApiModelProperty(value = "功能名称")
    var functionName: String,
    @ApiModelProperty(value = "模块名称")
    var moduleName: String,
    @ApiModelProperty(value = "模块api")
    var moduleApi: String,
    @ApiModelProperty(value = "是否有访问权限")
    var perm: Boolean = false,
)