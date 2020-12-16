package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.CustomPage
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class CustomPageDto(
    @ApiModelProperty(value = "api名称，唯一标识 ;;uk")
    var api: String,
    @ApiModelProperty(value = "自定义页面名称，用于后台查看")
    var name: String,
    @ApiModelProperty(value = "页面模板")
    var template: String,
    @ApiModelProperty(value = "是否为静态页面")
    var static: Boolean,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
){
    fun toEntity(): CustomPage {
        return CustomPage(
            api = api,
            name = name,
            template = template,
            static = static,
            remark = remark,
        )
    }

    fun fromEntity(entity: CustomPage): CustomPageDto {
        return entity.run {
            CustomPageDto(
                api = api,
                name = name,
                template = template,
                static = static,
                remark = remark,
          )
        }
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
