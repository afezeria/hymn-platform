package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dao.ModuleFunctionPermDao
import github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
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
    private lateinit var dbService: DatabaseService

    fun findByFunctionApi(functionApi: String): MutableList<ModuleFunctionPermDto> {
        return moduleFunctionPermDao.selectDto { it.functionApi eq functionApi }
    }

    fun findByRoleId(roleId: String): MutableList<ModuleFunctionPermDto> {
        return moduleFunctionPermDao.selectDto { it.roleId eq roleId }
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