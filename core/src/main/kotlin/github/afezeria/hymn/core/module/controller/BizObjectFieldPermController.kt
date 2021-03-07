package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import github.afezeria.hymn.core.module.service.BizObjectFieldPermService
import github.afezeria.hymn.core.module.service.BizObjectFieldService
import github.afezeria.hymn.core.module.service.RoleService
import github.afezeria.hymn.core.module.view.BizObjectFieldPermListView
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
        value = "根据 角色id和对象id 或 字段id 查询数据",
        notes = "(role_id,biz_object_id)和field_id同时只有一个启作用，(role_id,biz_object_id)优先，同时为空字符串时返回空列表"
    )
    @GetMapping
    fun find(
        @RequestParam("role_id", defaultValue = "") roleId: String,
        @RequestParam("biz_object_id", defaultValue = "") bizObjectId: String,
        @RequestParam("field_id", defaultValue = "") fieldId: String,
    ): List<BizObjectFieldPermListView> {
        return if (roleId.isNotBlank() && bizObjectId.isNotBlank()) {
            bizObjectFieldPermService.findViewByRoleIdAndBizObjectId(roleId, bizObjectId)
        } else if (fieldId.isNotBlank()) {
            bizObjectFieldPermService.findViewByFieldId(fieldId)
        } else {
            emptyList()
        }
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "修改字段权限", notes = "")
    @PutMapping
    fun save(@RequestBody dto: List<BizObjectFieldPermDto>): Int {
        return bizObjectFieldPermService.save(dto)
    }

}