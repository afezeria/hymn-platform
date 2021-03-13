package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.CustomPage
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class CustomPageDto(
    @ApiModelProperty(value = "api名称，唯一标识", required = true)
    var api: String,
    @ApiModelProperty(value = "自定义页面名称，用于后台查看", required = true)
    var name: String,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) {
    fun toEntity(): CustomPage {
        return CustomPage(
            api = api,
            name = name,
            remark = remark,
        )
    }

    fun update(entity: CustomPage) {
        entity.also {
            it.api = api
            it.name = name
            it.remark = remark
        }
    }
}
