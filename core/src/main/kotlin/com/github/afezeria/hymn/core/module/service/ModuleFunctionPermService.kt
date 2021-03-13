package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.common.util.msgByPair
import com.github.afezeria.hymn.core.module.dao.ModuleFunctionPermDao
import com.github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import com.github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import com.github.afezeria.hymn.core.module.view.ModuleFunctionPermListView
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ModuleFunctionPermService {

    @Autowired
    private lateinit var moduleFunctionPermDao: ModuleFunctionPermDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var moduleService: ModuleService

    @Autowired
    private lateinit var dbService: DatabaseService

    fun findByFunctionApi(functionApi: String): List<ModuleFunctionPermListView> {
        val function = moduleService.getFunctionByApi(functionApi)
            ?: throw DataNotFoundException("ModuleFunction".msgByPair("api", functionApi))
        val module = moduleService.getModuleByApi(function.moduleApi)
            ?: throw DataNotFoundException("Module".msgByPair("api", functionApi))
        val viewRoleIdMap =
            moduleFunctionPermDao.selectView { it.functionApi eq functionApi }
                .map { it.roleId to it }.toMap()
        val moduleName: String = module.name
        val moduleApi: String = module.api
        val functionName: String = function.name
        return roleService.findAll()
            .map {
                viewRoleIdMap[it.id] ?: ModuleFunctionPermListView(
                    roleId = it.id,
                    roleName = it.name,
                    functionApi = functionApi,
                    functionName = functionName,
                    moduleApi = moduleApi,
                    moduleName = moduleName,
                )
            }
    }

    fun findViewByRoleId(roleId: String): List<ModuleFunctionPermListView> {
        val role = roleService.findById(roleId)
            ?: throw DataNotFoundException("Role".msgById(roleId))
        val viewFunctionApiMap =
            moduleFunctionPermDao.selectView { it.roleId eq roleId }
                .map { it.functionApi to it }.toMap()
        val roleName = role.name
        val moduleApi2Name = moduleService.getAllModule().map { it.api to it.name }.toMap()
        return moduleService.getAllFunction()
            .map {
                viewFunctionApiMap[it.api] ?: ModuleFunctionPermListView(
                    roleId = roleId,
                    roleName = roleName,
                    functionApi = it.api,
                    functionName = it.name,
                    moduleName = requireNotNull(moduleApi2Name[it.moduleApi]),
                    moduleApi = it.moduleApi,
                )
            }
    }

    fun save(dtoList: List<ModuleFunctionPermDto>): Int {
        if (dtoList.isEmpty()) return 0
        return dbService.useTransaction {
            val inRoleIdSet = dtoList.mapTo(mutableSetOf()) { it.roleId }
            val availableRoleIdSet =
                roleService.findByIds(inRoleIdSet).mapTo(mutableSetOf()) { it.id }
//            function_api为关联到core_module_function表的外键，所有这里不需要对输入的function_api做校验
            val entityList = dtoList.mapNotNull {
                if (availableRoleIdSet.contains(it.roleId)) {
                    it.toEntity()
                } else {
                    null
                }
            }
            moduleFunctionPermDao.bulkInsertOrUpdate(
                entityList,
                *moduleFunctionPermDao.table.run {
                    arrayOf(roleId, functionApi)
                }
            )
        }

    }


    fun findByRoleIdAndFunctionApi(roleId: String, functionApi: String): ModuleFunctionPerm? {
        return moduleFunctionPermDao.singleRowSelect({ (it.roleId eq roleId) and (it.functionApi eq functionApi) })
    }
}