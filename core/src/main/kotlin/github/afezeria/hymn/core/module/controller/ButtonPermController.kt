package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.ButtonPermDto
import github.afezeria.hymn.core.module.service.ButtonPermService
import github.afezeria.hymn.core.module.service.CustomButtonService
import github.afezeria.hymn.core.module.service.RoleService
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
    ): List<ButtonPermDto> {
        return if (roleId.isNotBlank()) {
            if (bizObjectId.isNotBlank()) {
                val dtoButtonIdMap =
                    buttonPermService.findByRoleIdAndBizObjectId(roleId, bizObjectId)
                        .map { it.buttonId to it }.toMap()
                val objectName: String?
                val objectId: String?
                val roleName: String?
                dtoButtonIdMap.values.first().let {
                    objectName = it.bizObjectName
                    objectId = it.bizObjectId
                    roleName = it.buttonName
                }
                buttonService.findAll()
                    .map {
                        dtoButtonIdMap[it.id] ?: ButtonPermDto(roleId, it.id).apply {
                            this.bizObjectName = objectName
                            this.bizObjectId = objectId
                            buttonName = it.name
                            this.roleName = roleName
                        }
                    }
            } else {
                val dtoButtonIdMap = buttonPermService.findByRoleId(roleId)
                    .map { it.buttonId to it }.toMap()
                val roleName: String?
                dtoButtonIdMap.values.first().let {
                    roleName = it.roleName
                }
                buttonService.findAll()
                    .map {
                        dtoButtonIdMap[it.id] ?: ButtonPermDto(roleId, it.id).apply {
                            this.roleName = roleName
                            buttonName = it.name
                        }
                    }
            }
        } else if (buttonId.isNotBlank()) {
            val dtoRoleIdMap = buttonPermService.findByButtonId(buttonId)
                .map { it.roleId to it }.toMap()
            val objectName: String?
            val objectId: String?
            val buttonName: String?
            dtoRoleIdMap.values.first().let {
                objectName = it.bizObjectName
                objectId = it.bizObjectId
                buttonName = it.buttonName
            }
            roleService.findAll()
                .map {
                    dtoRoleIdMap[it.id] ?: ButtonPermDto(it.id, buttonId).apply {
                        roleName = it.name
                        this.bizObjectName = objectName
                        this.bizObjectId = objectId
                        this.buttonName = buttonName
                    }
                }
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