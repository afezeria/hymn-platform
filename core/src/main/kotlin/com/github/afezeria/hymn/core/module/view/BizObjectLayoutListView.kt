package com.github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@ApiModel(value = "业务对象页面布局列表")
class BizObjectLayoutListView(
    var id: String,
    @ApiModelProperty(value = "引用对象")
    var bizObjectId: String,
    @ApiModelProperty(value = "布局名称")
    var name: String,
    @ApiModelProperty(value = "备注")
    var remark: String? = null,
    @ApiModelProperty(value = "创建人id")
    var createById: String,
    @ApiModelProperty(value = "创建人")
    var createBy: String,
    @ApiModelProperty(value = "最后修改人id")
    var modifyById: String,
    @ApiModelProperty(value = "最后修改人")
    var modifyBy: String,
    @ApiModelProperty(value = "创建日期")
    var createDate: LocalDateTime,
    @ApiModelProperty(value = "最后修改日期")
    var modifyDate: LocalDateTime,
)