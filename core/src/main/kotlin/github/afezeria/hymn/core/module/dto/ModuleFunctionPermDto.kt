package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class ModuleFunctionPermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(value = "模块api ;;fk:[core_module cascade]", required = true)
    var moduleApi: String,
    @ApiModelProperty(value = "功能api ;;fk:[core_module_function cascade]", required = true)
    var functionApi: String,
    @ApiModelProperty(value = "是否有访问权限")
    var perm: Boolean,
) {
    fun toEntity(): ModuleFunctionPerm {
        return ModuleFunctionPerm(
            roleId = roleId,
            moduleApi = moduleApi,
            functionApi = functionApi,
            perm = perm,
        )
    }

    fun update(entity: ModuleFunctionPerm) {
        entity.also {
            it.roleId = roleId
            it.moduleApi = moduleApi
            it.functionApi = functionApi
            it.perm = perm
        }
    }
}
