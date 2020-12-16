package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.ButtonPerm
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class ButtonPermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "按钮id ;;fk:[core_custom_button cascade];idx")
    var buttonId: String,
    @ApiModelProperty(value = "是否可见")
    var visible: Boolean,
){
    fun toEntity(): ButtonPerm {
        return ButtonPerm(
            roleId = roleId,
            buttonId = buttonId,
            visible = visible,
        )
    }

    fun fromEntity(entity: ButtonPerm): ButtonPermDto {
        return entity.run {
            ButtonPermDto(
                roleId = roleId,
                buttonId = buttonId,
                visible = visible,
          )
        }
    }

    fun update(entity: ButtonPerm) {
        entity.also {
            it.roleId = roleId
            it.buttonId = buttonId
            it.visible = visible
        }
    }
}
