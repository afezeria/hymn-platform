package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.ButtonPerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class ButtonPermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(
        value = "按钮id，新建按钮时该字段用空字符串占位;;fk:[core_custom_button cascade];idx",
        required = true
    )
    var buttonId: String,
    @ApiModelProperty(value = "是否可见")
    var visible: Boolean = false,
) {
    @ApiModelProperty(value = "角色名称", notes = "只用于后端返回数据")
    var roleName: String? = null

    @ApiModelProperty(value = "按钮名称", notes = "只用于后端返回数据")
    var buttonName: String? = null

    @ApiModelProperty(value = "对象名称", notes = "只用于后端返回数据")
    var bizObjectName: String? = null

    @ApiModelProperty(value = "对象id", notes = "只用于后端返回数据")
    var bizObjectId: String? = null

    fun toEntity(): ButtonPerm {
        return ButtonPerm(
            roleId = roleId,
            buttonId = buttonId,
            visible = visible,
        )
    }

    fun update(entity: ButtonPerm) {
        entity.also {
            it.roleId = roleId
            it.buttonId = buttonId
            it.visible = visible
        }
    }
}
