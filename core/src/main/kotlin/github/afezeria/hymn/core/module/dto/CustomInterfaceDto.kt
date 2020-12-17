package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.CustomInterface
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class CustomInterfaceDto(
    @ApiModelProperty(value = "接口api名称，唯一标识 ;; uk")
    var api: String,
    @ApiModelProperty(value = "接口名称")
    var name: String,
    @ApiModelProperty(value = "接口代码")
    var code: String,
    @ApiModelProperty(value = "是否启用")
    var active: Boolean,
    @ApiModelProperty(value = "语言 ;; optional_value:[javascript]")
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）", required = true)
    var optionText: String? = null,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
){
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

    fun fromEntity(entity: CustomInterface): CustomInterfaceDto {
        return entity.run {
            CustomInterfaceDto(
                api = api,
                name = name,
                code = code,
                active = active,
                lang = lang,
                optionText = optionText,
                remark = remark,
          )
        }
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