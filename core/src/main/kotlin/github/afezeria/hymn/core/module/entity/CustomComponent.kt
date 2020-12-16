package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 自定义组件
 * @author afezeria
 */
@ApiModel(value="自定义组件",description = """自定义组件""")
data class CustomComponent(

    @ApiModelProperty(value = "api名称，唯一标识 ;; uk")
    var api: String,
    @ApiModelProperty(value = "组件在页面上的显示名称")
    var name: String,
    @ApiModelProperty(value = "组件html代码")
    var code: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
