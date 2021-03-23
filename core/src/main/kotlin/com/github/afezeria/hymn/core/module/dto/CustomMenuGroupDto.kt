package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.CustomMenuGroup
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class CustomMenuGroupDto(
    @ApiModelProperty(value = "分组名", required = true)
    var name: String,
) {
    fun toEntity(): CustomMenuGroup {
        return CustomMenuGroup(name)
    }

    fun update(entity: CustomMenuGroup) {
        entity.also {
            it.name = name
        }
    }
}
