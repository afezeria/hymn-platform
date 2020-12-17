package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.MenuItemPerm
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class MenuItemPermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "菜单项id ;;fk:[core_custom_menu_item cascade];idx")
    var menuItemId: String,
    @ApiModelProperty(value = "")
    var visible: Boolean,
){
    fun toEntity(): MenuItemPerm {
        return MenuItemPerm(
            roleId = roleId,
            menuItemId = menuItemId,
            visible = visible,
        )
    }

    fun fromEntity(entity: MenuItemPerm): MenuItemPermDto {
        return entity.run {
            MenuItemPermDto(
                roleId = roleId,
                menuItemId = menuItemId,
                visible = visible,
          )
        }
    }

    fun update(entity: MenuItemPerm) {
        entity.also {
            it.roleId = roleId
            it.menuItemId = menuItemId
            it.visible = visible
        }
    }
}