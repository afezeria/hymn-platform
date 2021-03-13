package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.ApiVersion
import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import com.github.afezeria.hymn.core.module.service.ModuleFunctionPermService
import com.github.afezeria.hymn.core.module.service.ModuleService
import com.github.afezeria.hymn.core.module.service.RoleService
import com.github.afezeria.hymn.core.module.view.ModuleFunctionPermListView
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
    ): List<ModuleFunctionPermListView> {
        return if (roleId.isNotBlank()) {
            moduleFunctionPermService.findViewByRoleId(roleId)
        } else if (functionApi.isNotBlank()) {
            moduleFunctionPermService.findByFunctionApi(functionApi)
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