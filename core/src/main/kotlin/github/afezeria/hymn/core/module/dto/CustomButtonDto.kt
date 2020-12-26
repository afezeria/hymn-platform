package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.CustomButton
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class CustomButtonDto(
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
    @ApiModelProperty(value = "业务对象id，不为空时表示该按钮只能在该对象相关页面中使用;idx", required = true)
    var bizObjectId: String? = null,
    @ApiModelProperty(value = "")
    var name: String,
    @ApiModelProperty(value = "唯一标识 ;; uk")
    var api: String,
    @ApiModelProperty(value = "客户端类型，表示只能用在特定类型客户端中 ;; optional_value:[browser(pc端), mobile(移动端)]")
    var clientType: String,
    @ApiModelProperty(value = "按钮行为 ;; optional_value:[eval(执行js代码),open_in_current_tab(在当前页面中打开链接),open_in_new_tab(在新标签页中打开链接),open_in_new_window(在新窗口中打开链接)]")
    var action: String,
    @ApiModelProperty(value = "按钮内容，当action为eval时为js代码，其他情况为url")
    var content: String,
    @ApiModelProperty(value = "字段权限")
    var permList: List<ButtonPermDto> = emptyList()
) {
    fun toEntity(): CustomButton {
        return CustomButton(
            remark = remark,
            bizObjectId = bizObjectId,
            name = name,
            api = api,
            clientType = clientType,
            action = action,
            content = content,
        )
    }

    fun update(entity: CustomButton) {
        entity.also {
            it.remark = remark
            it.bizObjectId = bizObjectId
            it.name = name
            it.api = api
            it.clientType = clientType
            it.action = action
            it.content = content
        }
    }
}
