package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import github.afezeria.hymn.core.module.service.BizObjectFieldPermService
import github.afezeria.hymn.core.module.service.BizObjectFieldService
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
@RequestMapping("/core/api/{version}/biz-object-field-perm")
@Api(tags = ["BizObjectFieldPermController"], description = "字段权限接口")
class BizObjectFieldPermController {

    @Autowired
    private lateinit var bizObjectFieldPermService: BizObjectFieldPermService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var fieldService: BizObjectFieldService

    @Function(AccountType.ADMIN)
    @ApiOperation(
        value = "根据角色id或字段id查询数据",
        notes = "role_id和field_id同时只有一个启作用，role_id优先，同时为空字符串时返回空列表"
    )
    @GetMapping
    fun find(
        @RequestParam("role_id") roleId: String,
        @RequestParam("field_id") fieldId: String,
    ): List<BizObjectFieldPermDto> {
        return if (roleId.isNotBlank()) {
            bizObjectFieldPermService.findByRoleId(roleId)
        } else if (fieldId.isNotBlank()) {
            bizObjectFieldPermService.findByFieldId(fieldId)
        } else {
            emptyList()
        }
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "修改字段权限", notes = "")
    @PostMapping
    fun create(@RequestBody dto: List<BizObjectFieldPermDto>): Int {
        return bizObjectFieldPermService.save(dto)
    }

}