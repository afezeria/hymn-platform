package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectMappingListView(
    var id: String,
    @ApiModelProperty(value = "源对象id")
    var sourceBizObjectId: String,
    @ApiModelProperty(value = "源对象名称")
    var sourceBizObjectName: String,
    @ApiModelProperty(value = "源对象记录类型id")
    var sourceTypeId: String,
    @ApiModelProperty(value = "源对象记录类型名称")
    var sourceTypeName: String,
    @ApiModelProperty(value = "目标对象id")
    var targetBizObjectId: String,
    @ApiModelProperty(value = "目标对象名称")
    var targetBizObjectName: String,
    @ApiModelProperty(value = "目标对象记录类型id")
    var targetTypeId: String,
    @ApiModelProperty(value = "目标对象记录类型名称")
    var targetTypeName: String,
)