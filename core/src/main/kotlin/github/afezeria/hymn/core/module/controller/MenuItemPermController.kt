package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
import github.afezeria.hymn.core.module.service.CustomMenuItemService
import github.afezeria.hymn.core.module.service.MenuItemPermService
import github.afezeria.hymn.core.module.service.RoleService
import github.afezeria.hymn.core.module.view.MenuItemPermListView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/menu-item-perm")
@Api(tags = ["MenuItemPermController"], description = "菜单项权限接口")
class MenuItemPermController {

    @Autowired
    private lateinit var menuItemPermService: MenuItemPermService

    @Autowired
    private lateinit var menuItemService: CustomMenuItemService

    @Autowired
    private lateinit var roleService: RoleService


    @Function(AccountType.ADMIN)
    @ApiOperation(
        value = "查询按钮权限",
        notes = "role_id不为空时返回该角色对所有菜单项的权限,item_id不为空时返回所有角色对该菜单项的权限"
    )
    @GetMapping
    fun find(
        @RequestParam("role_id", defaultValue = "") roleId: String,
        @RequestParam("item_id", defaultValue = "") itemId: String,
    ): List<MenuItemPermListView> {
        return if (roleId.isNotBlank()) {
            menuItemPermService.findViewByRoleId(roleId)
        } else if (itemId.isNotBlank()) {
            menuItemPermService.findViewByItemId(itemId)
        } else {
            emptyList()
        }
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "修改菜单项权限", notes = "")
    @PutMapping
    fun save(@RequestBody dtoList: List<MenuItemPermDto>): Int {
        return menuItemPermService.save(dtoList)
    }
}