package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class ObjectTypeLayoutListView(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(value = "记录类型id ;;fk:[core_biz_object_type cascade]", required = true)
    var typeId: String,
    @ApiModelProperty(value = "页面布局id ;;fk:[core_biz_object_layout cascade]", required = true)
    var layoutId: String,
    @ApiModelProperty(value = "角色名称", notes = "只用于后端返回数据")
    var roleName: String,
    @ApiModelProperty(value = "类型名称", notes = "只用于后端返回数据")
    var typeName: String,
    @ApiModelProperty(value = "布局名称", notes = "只用于后端返回数据")
    var layoutName: String,
)