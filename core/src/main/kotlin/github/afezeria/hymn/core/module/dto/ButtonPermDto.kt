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
    @ApiModelProperty(value = "按钮id，新建按钮时该字段用空字符串占位;;fk:[core_custom_button cascade];idx")
    var buttonId: String,
    @ApiModelProperty(value = "是否可见")
    var visible: Boolean=false,
){
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
