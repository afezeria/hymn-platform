package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 自定义接口
 * @author afezeria
 */
@ApiModel(value = "自定义接口", description = """自定义接口""")
data class CustomInterface(

    @ApiModelProperty(value = "接口api名称，唯一标识 ")
    var api: String,
    @ApiModelProperty(value = "接口名称")
    var name: String,
    @ApiModelProperty(value = "接口代码")
    var code: String,
    @ApiModelProperty(value = "是否启用")
    var active: Boolean,
    @ApiModelProperty(value = "语言 ;; optional_value:[javascript]")
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）")
    var optionText: String? = null,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
