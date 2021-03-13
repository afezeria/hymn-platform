package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.CustomInterface
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class CustomInterfaceDto(
    @ApiModelProperty(value = "接口api名称，唯一标识", required = true)
    var api: String,
    @ApiModelProperty(value = "接口名称", required = true)
    var name: String,
    @ApiModelProperty(value = "接口代码", required = true)
    var code: String,
    @ApiModelProperty(value = "是否启用", required = true)
    var active: Boolean,
    @ApiModelProperty(value = "语言 ;; optional_value:[javascript]", required = true)
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）")
    var optionText: String? = null,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) {
    fun toEntity(): CustomInterface {
        return CustomInterface(
            api = api,
            name = name,
            code = code,
            active = active,
            lang = lang,
            optionText = optionText,
            remark = remark,
        )
    }

    fun update(entity: CustomInterface) {
        entity.also {
            it.api = api
            it.name = name
            it.code = code
            it.active = active
            it.lang = lang
            it.optionText = optionText
            it.remark = remark
        }
    }
}
