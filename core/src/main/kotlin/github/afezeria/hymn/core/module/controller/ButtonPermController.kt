package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.ButtonPermDto
import github.afezeria.hymn.core.module.service.ButtonPermService
import github.afezeria.hymn.core.module.service.CustomButtonService
import github.afezeria.hymn.core.module.service.RoleService
import github.afezeria.hymn.core.module.view.ButtonPermListView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/button-perm")
@Api(tags = ["ButtonPermController"], description = "按钮权限接口")
class ButtonPermController {

    @Autowired
    private lateinit var buttonPermService: ButtonPermService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var buttonService: CustomButtonService

    @Function(AccountType.ADMIN)
    @ApiOperation(
        value = "查询按钮权限",
        notes = "role_id和biz_object_id不为空时返回该角色下专属于该对象的按钮的权限,role_id不为空时返回该角色对所有通用按钮的权限,button_id不为空时返回所有角色对该按钮的权限"
    )
    @GetMapping
    fun find(
        @RequestParam("role_id", defaultValue = "") roleId: String,
        @RequestParam("biz_object_id", defaultValue = "") bizObjectId: String,
        @RequestParam("button_id", defaultValue = "") buttonId: String,
    ): List<ButtonPermListView> {
        return if (roleId.isNotBlank()) {
            if (bizObjectId.isNotBlank()) {
                buttonPermService.findViewByRoleIdAndBizObjectId(roleId, bizObjectId)
            } else {
                buttonPermService.findViewByRoleId(roleId)
            }
        } else if (buttonId.isNotBlank()) {
            buttonPermService.findViewByButtonId(buttonId)
        } else {
            emptyList()
        }
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "修改按钮权限", notes = "")
    @PutMapping
    fun save(@RequestBody dtoList: List<ButtonPermDto>): Int {
        return buttonPermService.save(dtoList)
    }

}