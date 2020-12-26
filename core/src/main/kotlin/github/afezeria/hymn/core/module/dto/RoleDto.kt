package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.Role
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class RoleDto(
    @ApiModelProperty(value = "角色名称")
    var name: String,
    @ApiModelProperty(value = "", required = true)
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
