package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.CustomMenuItem
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class CustomMenuItemDto(
    @ApiModelProperty(value = "唯一标识", required = true)
    var api: String,
    @ApiModelProperty(value = "分组id")
    var groupId: String? = null,
    @ApiModelProperty(value = "菜单项名称", required = true)
    var name: String,
    @ApiModelProperty(value = "url path", required = true)
    var path: String,
    @ApiModelProperty(value = "path类型 ;; optional_value:[path(路径),url(外部url)]", required = true)
    var pathType: String,
    @ApiModelProperty(
        value = "菜单点击行为 ;; optional_value:[iframe(在iframe中打开), current_tab(当前标签页中打开), new_tab(新标签页中打开)]",
        required = true
    )
    var action: String,
    @ApiModelProperty(
        value = "客户端类型  ;; optional_value:[browser(浏览器), mobile(移动端)]",
        required = true
    )
    var clientType: String,
    @ApiModelProperty(value = "图标", required = true)
    var icon: String,
) {
    fun toEntity(): CustomMenuItem {
        return CustomMenuItem(
            name = name,
            groupId = groupId,
            path = path,
            pathType = pathType,
            action = action,
            clientType = clientType,
            icon = icon,
            api = api,
        )
    }

    fun update(entity: CustomMenuItem) {
        entity.also {
            it.name = name
            it.api = api
            it.path = path
            it.groupId = groupId
            it.pathType = pathType
            it.action = action
            it.clientType = clientType
            it.icon = icon
        }
    }
}
