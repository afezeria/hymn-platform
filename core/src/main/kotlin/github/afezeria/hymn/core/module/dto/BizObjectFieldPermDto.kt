package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectFieldPermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(
        value = "字段id，新建自定义字段时该字段用空字符串占位 ;;fk:[core_biz_object_field cascade];idx",
        required = true
    )
    var fieldId: String,
    @ApiModelProperty(value = "可读")
    var pRead: Boolean = false,
    @ApiModelProperty(value = "可编辑")
    var pEdit: Boolean = false,
) {
    fun toEntity(): BizObjectFieldPerm {
        return BizObjectFieldPerm(
            roleId = roleId,
            fieldId = fieldId,
            pRead = pRead,
            pEdit = pEdit,
        )
    }

    fun update(entity: BizObjectFieldPerm) {
        entity.also {
            it.roleId = roleId
            it.fieldId = fieldId
            it.pRead = pRead
            it.pEdit = pEdit
        }
    }
}
