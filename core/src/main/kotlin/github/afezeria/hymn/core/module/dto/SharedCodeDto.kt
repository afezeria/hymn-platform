package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.SharedCode
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class SharedCodeDto(
    @ApiModelProperty(value = "api名称,也是代码中的函数名称 ;;uk", required = true)
    var api: String,
    @ApiModelProperty(value = "代码类型 ;;optional_value:[function(函数代码),job(任务代码)]", required = true)
    var type: String,
    @ApiModelProperty(value = "代码", required = true)
    var code: String,
    @ApiModelProperty(value = "语言 ;;optional_value:[javascript]", required = true)
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）")
    var optionText: String? = null,
) {
    fun toEntity(): SharedCode {
        return SharedCode(
            api = api,
            type = type,
            code = code,
            lang = lang,
            optionText = optionText,
        )
    }

    fun update(entity: SharedCode) {
        entity.also {
            it.api = api
            it.type = type
            it.code = code
            it.lang = lang
            it.optionText = optionText
        }
    }
}
