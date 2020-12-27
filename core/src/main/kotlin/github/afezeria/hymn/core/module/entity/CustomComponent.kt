package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 自定义组件
 * @author afezeria
 */
@ApiModel(value = "自定义组件", description = """自定义组件""")
data class CustomComponent(

    @ApiModelProperty(value = "api名称，唯一标识 ;; uk", required = true)
    var api: String,
    @ApiModelProperty(value = "组件在页面上的显示名称", required = true)
    var name: String,
    @ApiModelProperty(value = "组件html代码", required = true)
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
