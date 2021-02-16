package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 自定义按钮
 * @author afezeria
 */
@ApiModel(value = "自定义按钮", description = """自定义按钮""")
data class CustomButton(

    @ApiModelProperty(value = "")
    var remark: String? = null,
    @ApiModelProperty(value = "业务对象id，不为空时表示该按钮只能在该对象相关页面中使用;idx")
    var bizObjectId: String? = null,
    @ApiModelProperty(value = "", required = true)
    var name: String,
    @ApiModelProperty(value = "唯一标识 ;; uk", required = true)
    var api: String,
    @ApiModelProperty(
        value = "客户端类型，表示只能用在特定类型客户端中 ;; optional_value:[browser(pc端), mobile(移动端)]",
        required = true
    )
    var clientType: String,
    @ApiModelProperty(
        value = "按钮行为 ;; optional_value:[eval(执行js代码),open_in_current_tab(在当前页面中打开链接),open_in_new_tab(在新标签页中打开链接),open_in_new_window(在新窗口中打开链接)]",
        required = true
    )
    var action: String,
    @ApiModelProperty(value = "按钮内容，当action为eval时为js代码，其他情况为url", required = true)
    var content: String,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
