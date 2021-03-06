package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.Role
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class RoleDto(
    @ApiModelProperty(value = "角色名称", required = true)
    var name: String,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) {
    fun toEntity(): Role {
        return Role(
            name = name,
            remark = remark,
        )
    }

    fun update(entity: Role) {
        entity.also {
            it.name = name
            it.remark = remark
        }
    }
}
