package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypePermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade]")
    var roleId: String,
    @ApiModelProperty(value = "对象id ;;fk:[core_biz_object cascade]")
    var bizObjectId: String,
    @ApiModelProperty(value = "类型id ;;fk:[core_biz_object_type cascade];idx")
    var typeId: String,
    @ApiModelProperty(value = "创建数据时选择特定记录类型的权限")
    var visible: Boolean,
) {
    fun toEntity(): BizObjectTypePerm {
        return BizObjectTypePerm(
            roleId = roleId,
            bizObjectId = bizObjectId,
            typeId = typeId,
            visible = visible,
        )
    }

    fun update(entity: BizObjectTypePerm) {
        entity.also {
            it.roleId = roleId
            it.bizObjectId = bizObjectId
            it.typeId = typeId
            it.visible = visible
        }
    }
}
