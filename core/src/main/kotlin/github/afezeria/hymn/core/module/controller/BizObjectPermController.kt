package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.BizObjectPermDto
import github.afezeria.hymn.core.module.service.BizObjectPermService
import github.afezeria.hymn.core.module.service.BizObjectService
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
@RequestMapping("/core/api/{version}/biz-object-perm")
@Api(tags = ["BizObjectPermController"], description = "对象权限接口")
class BizObjectPermController {

    @Autowired
    private lateinit var bizObjectPermService: BizObjectPermService

    @Autowired
    private lateinit var bizObjectService: BizObjectService

    @Autowired
    private lateinit var roleService: RoleService


    @Function(AccountType.ADMIN)
    @ApiOperation(
        value = "根据角色id或对象id查询",
        notes = "role_id和biz_object_id同时只有一个启作用，role_id优先，同时为空字符串时返回空列表"
    )
    @GetMapping
    fun find(
        @RequestParam("role_id", defaultValue = "") roleId: String,
        @RequestParam("biz_object_id", defaultValue = "") bizObjectId: String,
    ): List<BizObjectPermDto> {
        return if (roleId.isNotBlank()) {
            val dtoObjectIdMap = bizObjectPermService.findDtoByRoleId(roleId)
                .map { it.bizObjectId to it }.toMap()
            val roleName = dtoObjectIdMap.values.first().roleName
            bizObjectService.findAllActiveObject()
                .map {
                    dtoObjectIdMap[it.id] ?: BizObjectPermDto(roleId, it.id).apply {
                        this.roleName = roleName
                        bizObjectName = it.name
                    }
                }
        } else if (bizObjectId.isNotBlank()) {
            val dtoRoleIdMap = bizObjectPermService.findDtoByBizObjectId(bizObjectId)
                .map { it.roleId to it }.toMap()
            val objectName = dtoRoleIdMap.values.first().bizObjectName
            roleService.findAll()
                .map {
                    dtoRoleIdMap[it.id] ?: BizObjectPermDto(it.id, bizObjectId).apply {
                        roleName = it.name
                        bizObjectName = objectName
                    }
                }
        } else {
            emptyList()
        }
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "修改对象权限", notes = "")
    @PutMapping
    fun save(@RequestBody dtoList: List<BizObjectPermDto>): Int {
        val id = bizObjectPermService.save(dtoList)
        return id
    }

}