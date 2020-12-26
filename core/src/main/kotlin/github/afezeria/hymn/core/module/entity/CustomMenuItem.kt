package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 菜单项
 * @author afezeria
 */
@ApiModel(value = "菜单项", description = """菜单项""")
data class CustomMenuItem(

    @ApiModelProperty(value = "菜单项名称")
    var name: String,
    @ApiModelProperty(value = "url path")
    var path: String,
    @ApiModelProperty(value = "path类型 ;; optional_value:[path(路径),url(外部url)]")
    var pathType: String,
    @ApiModelProperty(value = "菜单点击行为 ;; optional_value:[iframe(在iframe中打开), current_tab(当前标签页中打开), new_tab(新标签页中打开)]")
    var action: String,
    @ApiModelProperty(value = "客户端类型  ;; optional_value:[browser(浏览器), mobile(移动端)]")
    var clientType: String,
    @ApiModelProperty(value = "图标")
    var icon: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
