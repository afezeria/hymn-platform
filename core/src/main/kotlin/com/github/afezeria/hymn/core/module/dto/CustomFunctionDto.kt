package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.CustomFunction
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class CustomFunctionDto(
    @ApiModelProperty(value = "api名称,也是代码中的函数名称", required = true)
    var api: String,
    @ApiModelProperty(value = "代码类型 ;;optional_value:[function(函数代码),job(任务代码)]", required = true)
    var type: String,
    @ApiModelProperty(value = "代码", required = true)
    var code: String,
    @ApiModelProperty(value = "是否是基础函数，只有基础函数能够被其他脚本代码引用，并且基础函数不能引用其他自定义函数")
    var baseFun: Boolean = true,
    @ApiModelProperty(value = "语言 ;;optional_value:[javascript]", required = true)
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）")
    var optionText: String? = null,
) {
    fun toEntity(): CustomFunction {
        return CustomFunction(
            api = api,
            type = type,
            code = code,
            baseFun = baseFun,
            lang = lang,
            optionText = optionText,
        )
    }

    fun update(entity: CustomFunction) {
        entity.also {
            it.api = api
            it.type = type
            it.code = code
            it.baseFun = baseFun
            it.lang = lang
            it.optionText = optionText
        }
    }
}
