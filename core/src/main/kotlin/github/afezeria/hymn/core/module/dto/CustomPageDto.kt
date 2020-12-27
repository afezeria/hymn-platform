package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.CustomPage
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class CustomPageDto(
    @ApiModelProperty(value = "api名称，唯一标识 ;;uk", required = true)
    var api: String,
    @ApiModelProperty(value = "自定义页面名称，用于后台查看", required = true)
    var name: String,
    @ApiModelProperty(value = "页面模板", required = true)
    var template: String,
    @ApiModelProperty(value = "是否为静态页面", required = true)
    var static: Boolean,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) {
    fun toEntity(): CustomPage {
        return CustomPage(
            api = api,
            name = name,
            template = template,
            static = static,
            remark = remark,
        )
    }

    fun update(entity: CustomPage) {
        entity.also {
            it.api = api
            it.name = name
            it.template = template
            it.static = static
            it.remark = remark
        }
    }
}
