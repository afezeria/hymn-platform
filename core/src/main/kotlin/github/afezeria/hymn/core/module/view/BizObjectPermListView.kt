package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectPermListView(
    @ApiModelProperty(value = "角色id")
    var roleId: String,
    @ApiModelProperty(value = "对象id")
    var bizObjectId: String,
    @ApiModelProperty(value = "角色名称")
    var roleName: String,
    @ApiModelProperty(value = "对象名称")
    var bizObjectName: String,
    @ApiModelProperty(value = "创建")
    var ins: Boolean = false,
    @ApiModelProperty(value = "更新")
    var upd: Boolean = false,
    @ApiModelProperty(value = "删除")
    var del: Boolean = false,
    @ApiModelProperty(value = "查看")
    var que: Boolean = false,
    @ApiModelProperty(value = "查看本人及直接下属")
    var queryWithAccountTree: Boolean = false,
    @ApiModelProperty(value = "查看本部门")
    var queryWithOrg: Boolean = false,
    @ApiModelProperty(value = "查看本部门及下级部门")
    var queryWithOrgTree: Boolean = false,
    @ApiModelProperty(value = "查看全部")
    var queryAll: Boolean = false,
    @ApiModelProperty(value = "编辑全部")
    var editAll: Boolean = false,
)