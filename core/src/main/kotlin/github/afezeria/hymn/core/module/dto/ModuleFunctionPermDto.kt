package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class ModuleFunctionPermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "功能id ;;fk:[core_module_function cascade];idx")
    var moduleFunctionId: String,
    @ApiModelProperty(value = "是否有访问权限", required = true)
    var perm: Boolean? = null,
){
    fun toEntity(): ModuleFunctionPerm {
        return ModuleFunctionPerm(
            roleId = roleId,
            moduleFunctionId = moduleFunctionId,
            perm = perm,
        )
    }

    fun fromEntity(entity: ModuleFunctionPerm): ModuleFunctionPermDto {
        return entity.run {
            ModuleFunctionPermDto(
                roleId = roleId,
                moduleFunctionId = moduleFunctionId,
                perm = perm,
          )
        }
    }

    fun update(entity: ModuleFunctionPerm) {
        entity.also {
            it.roleId = roleId
            it.moduleFunctionId = moduleFunctionId
            it.perm = perm
        }
    }
}
