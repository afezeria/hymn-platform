package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 自定义页面
 * @author afezeria
 */
@ApiModel(value = "自定义页面", description = """自定义页面""")
data class CustomPage(

    @ApiModelProperty(value = "api名称，唯一标识 ;;uk")
    var api: String,
    @ApiModelProperty(value = "自定义页面名称，用于后台查看")
    var name: String,
    @ApiModelProperty(value = "页面模板")
    var template: String,
    @ApiModelProperty(value = "是否为静态页面")
    var static: Boolean,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
