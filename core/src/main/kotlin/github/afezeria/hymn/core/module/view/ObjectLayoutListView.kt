package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@ApiModel(value = "业务对象页面布局列表")
class ObjectLayoutListView(
    var id: String,
    @ApiModelProperty(value = "引用对象", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "布局名称", required = true)
    var name: String,
    @ApiModelProperty(value = "备注", required = true)
    var remark: String? = null,
    @ApiModelProperty(value = "创建人id", required = true)
    var createById: String,
    @ApiModelProperty(value = "创建人", required = true)
    var createBy: String,
    @ApiModelProperty(value = "最后修改人id", required = true)
    var modifyById: String,
    @ApiModelProperty(value = "最后修改人", required = true)
    var modifyBy: String,
    @ApiModelProperty(value = "创建日期", required = true)
    var createDate: LocalDateTime,
    @ApiModelProperty(value = "最后修改日期", required = true)
    var modifyDate: LocalDateTime,
)