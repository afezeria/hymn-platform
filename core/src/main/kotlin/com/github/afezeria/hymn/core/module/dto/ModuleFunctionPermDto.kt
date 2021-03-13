package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class ModuleFunctionPermDto(
    @ApiModelProperty(value = "角色id", required = true)
    var roleId: String,
    @ApiModelProperty(value = "功能api", required = true)
    var functionApi: String,
    @ApiModelProperty(value = "是否有访问权限")
    var perm: Boolean = false,
) {

    fun toEntity(): ModuleFunctionPerm {
        return ModuleFunctionPerm(
            roleId = roleId,
            functionApi = functionApi,
            perm = perm,
        )
    }

    fun update(entity: ModuleFunctionPerm) {
        entity.also {
            it.roleId = roleId
            it.functionApi = functionApi
            it.perm = perm
        }
    }
}
