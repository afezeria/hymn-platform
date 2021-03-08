package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.CustomComponent
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class CustomComponentDto(
    @ApiModelProperty(value = "api名称，唯一标识", required = true)
    var api: String,
    @ApiModelProperty(value = "组件在页面上的显示名称", required = true)
    var name: String,
    @ApiModelProperty(value = "组件html代码", required = true)
    var code: String,
) {
    fun toEntity(): CustomComponent {
        return CustomComponent(
            api = api,
            name = name,
            code = code,
        )
    }

    fun update(entity: CustomComponent) {
        entity.also {
            it.api = api
            it.name = name
            it.code = code
        }
    }
}
