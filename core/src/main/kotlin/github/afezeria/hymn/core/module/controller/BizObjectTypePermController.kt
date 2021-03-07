package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import github.afezeria.hymn.core.module.service.BizObjectTypePermService
import github.afezeria.hymn.core.module.service.BizObjectTypeService
import github.afezeria.hymn.core.module.service.RoleService
import github.afezeria.hymn.core.module.view.BizObjectTypePermListView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/biz-object-type-perm")
@Api(tags = ["BizObjectTypePermController"], description = "记录类型权限接口")
class BizObjectTypePermController {

    @Autowired
    private lateinit var bizObjectTypePermService: BizObjectTypePermService

    @Autowired
    private lateinit var bizObjectTypeService: BizObjectTypeService

    @Autowired
    private lateinit var roleService: RoleService


    @Function(AccountType.ADMIN)
    @ApiOperation(
        value = "根据类型id或角色id和对象id查询",
        notes = "(role_id,biz_object_id)和type_id同时只有一个启作用，(role_id,biz_object_id)优先，同时为空字符串时返回空列表"
    )
    @GetMapping
    fun find(
        @RequestParam("role_id", defaultValue = "") roleId: String,
        @RequestParam("biz_object_id", defaultValue = "") bizObjectId: String,
        @RequestParam("type_id", defaultValue = "") typeId: String,
    ): List<BizObjectTypePermListView> {
        return if (roleId.isNotBlank() && bizObjectId.isNotBlank()) {
            bizObjectTypePermService.findViewByRoleIdAndBizObjectId(roleId, bizObjectId)
        } else if (typeId.isNotBlank()) {
            bizObjectTypePermService.findViewByTypeId(typeId)
        } else {
            emptyList()
        }
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "修改类型权限", notes = "")
    @PutMapping
    fun save(@RequestBody dtoList: List<BizObjectTypePermDto>): Int {
        return bizObjectTypePermService.save(dtoList)
    }

}