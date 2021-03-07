package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypeFieldOptionListView(
    @ApiModelProperty(value = "所属对象")
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型id")
    var typeId: String,
    @ApiModelProperty(value = "字段id")
    var fieldId: String,
    @ApiModelProperty(value = "字段名称")
    var fieldName: String,
    @ApiModelProperty(value = "字段关联的字典项id")
    var dictItemId: String,
    @ApiModelProperty(value = "字段关联的字典项名称")
    var dictItemName: String,
    @ApiModelProperty(value = "字段关联的字典项代码")
    var dictItemCode: String,
)