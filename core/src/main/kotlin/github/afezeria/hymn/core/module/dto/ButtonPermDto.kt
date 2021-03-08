package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.ButtonPerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class ButtonPermDto(
    @ApiModelProperty(value = "角色id", required = true)
    var roleId: String,
    @ApiModelProperty(
        value = "按钮id，新建按钮时该字段用空字符串占位",
        required = true
    )
    var buttonId: String,
    @ApiModelProperty(value = "是否可见")
    var visible: Boolean = false,
) {

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
