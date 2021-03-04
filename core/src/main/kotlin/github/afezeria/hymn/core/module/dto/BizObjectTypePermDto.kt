package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypePermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade]", required = true)
    var roleId: String,
    @ApiModelProperty(
        value = "类型id，新建类型时使用空字符串占位 ;;fk:[core_biz_object_type cascade];idx",
        required = true
    )
    var typeId: String,
    @ApiModelProperty(value = "创建数据时选择特定记录类型的权限", required = true)
    var visible: Boolean = false,
) {
    @ApiModelProperty(value = "角色名称", notes = "只用于后端返回数据")
    var roleName: String? = null

    @ApiModelProperty(value = "类型名称", notes = "只用于后端返回数据")
    var typeName: String? = null
    fun toEntity(): BizObjectTypePerm {
        return BizObjectTypePerm(
            roleId = roleId,
            typeId = typeId,
            visible = visible,
        )
    }

    fun update(entity: BizObjectTypePerm) {
        entity.also {
            it.roleId = roleId
            it.typeId = typeId
            it.visible = visible
        }
    }
}
