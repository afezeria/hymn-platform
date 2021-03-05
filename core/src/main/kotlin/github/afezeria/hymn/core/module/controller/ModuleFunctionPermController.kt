package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import github.afezeria.hymn.core.module.service.ModuleFunctionPermService
import github.afezeria.hymn.core.module.service.ModuleService
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
@RequestMapping("/core/api/{version}/module-function-perm")
@Api(tags = ["ModuleFunctionPermController"], description = "模块功能权限表接口")
class ModuleFunctionPermController {

    @Autowired
    private lateinit var moduleFunctionPermService: ModuleFunctionPermService

    @Autowired
    private lateinit var moduleService: ModuleService

    @Autowired
    private lateinit var roleService: RoleService


    @Function(AccountType.ADMIN)
    @ApiOperation(
        value = "查询按钮权限",
        notes = "role_id不为空时返回该角色对所有功能的权限,function_api不为空时返回所有角色对该功能的权限"
    )
    @GetMapping
    fun find(
        @RequestParam("role_id", defaultValue = "") roleId: String,
        @RequestParam("function_api", defaultValue = "") functionApi: String,
    ): List<ModuleFunctionPermDto> {
        return if (roleId.isNotBlank()) {
            val dtoFunctionApiMap =
                moduleFunctionPermService.findByRoleId(roleId)
                    .map { it.functionApi to it }.toMap()
            val roleName = dtoFunctionApiMap.values.first().roleName
            val moduleApi2Name = moduleService.getAllModule().map { it.api to it.name }.toMap()
            moduleService.getAllFunction()
                .map {
                    dtoFunctionApiMap[it.api] ?: ModuleFunctionPermDto(roleId, it.api).apply {
                        this.roleName = roleName
                        this.moduleApi = it.moduleApi
                        this.moduleName = moduleApi2Name[it.moduleApi]
                        functionName = it.name
                    }
                }
        } else if (functionApi.isNotBlank()) {
            val dtoRoleIdMap = moduleFunctionPermService.findByFunctionApi(functionApi)
                .map { it.roleId to it }.toMap()
            val moduleName: String?
            val moduleApi: String?
            val functionName: String?
            dtoRoleIdMap.values.first().let {
                moduleApi = it.moduleApi
                moduleName = it.moduleName
                functionName = it.functionName
            }
            roleService.findAll()
                .map {
                    dtoRoleIdMap[it.id] ?: ModuleFunctionPermDto(it.id, functionApi).apply {
                        roleName = it.name
                        this.moduleApi = moduleApi
                        this.moduleName = moduleName
                        this.functionName = functionName
                    }
                }
        } else {
            emptyList()
        }
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "修改模块功能权限", notes = "")
    @PutMapping
    fun save(@RequestBody dtoList: List<ModuleFunctionPermDto>): Int {
        return moduleFunctionPermService.save(dtoList)
    }

}