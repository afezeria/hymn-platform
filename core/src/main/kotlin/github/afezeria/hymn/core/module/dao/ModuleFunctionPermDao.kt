package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import github.afezeria.hymn.core.module.table.CoreModuleFunctionPerms
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class ModuleFunctionPermDao(
    databaseService: DatabaseService
) : AbstractDao<ModuleFunctionPerm, CoreModuleFunctionPerms>(
    table = CoreModuleFunctionPerms(),
    databaseService = databaseService
) {


    fun selectByRoleIdAndModuleApiAndFunctionApi(
        roleId: String,
        moduleApi: String,
        functionApi: String,
    ): ModuleFunctionPerm? {
        return singleRowSelect(
            listOf(
                table.roleId eq roleId,
                table.moduleApi eq moduleApi,
                table.functionApi eq functionApi
            )
        )
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<ModuleFunctionPerm> {
        return select({ it.roleId eq roleId })
    }


}