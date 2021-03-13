package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import com.github.afezeria.hymn.core.module.table.CoreModuleFunctionPerms
import com.github.afezeria.hymn.core.module.table.CoreModuleFunctions
import com.github.afezeria.hymn.core.module.table.CoreModules
import com.github.afezeria.hymn.core.module.table.CoreRoles
import com.github.afezeria.hymn.core.module.view.ModuleFunctionPermListView
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
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

    fun selectByRoleId(
        roleId: String,
    ): MutableList<ModuleFunctionPerm> {
        return select({ it.roleId eq roleId })
    }

    private val coreModuleFunctions = CoreModuleFunctions()
    private val coreModules = CoreModules()
    private val roles = CoreRoles()

    fun selectView(whereExpr: (CoreModuleFunctionPerms) -> ColumnDeclaring<Boolean>): MutableList<ModuleFunctionPermListView> {
        return table.run {
            databaseService.db().from(this)
                .innerJoin(roles, roles.id eq roleId)
                .innerJoin(coreModuleFunctions, coreModuleFunctions.api eq functionApi)
                .innerJoin(coreModules, coreModules.api eq coreModuleFunctions.moduleApi)
                .select(
                    roleId,
                    functionApi,
                    roles.name,
                    coreModules.name,
                    coreModules.api,
                    coreModuleFunctions.name,
                )
                .where {
                    whereExpr(this)
                }
                .mapTo(ArrayList()) {
                    ModuleFunctionPermListView(
                        roleId = requireNotNull(it[roleId]),
                        roleName = requireNotNull(it[roles.name]),
                        functionApi = requireNotNull(it[functionApi]),
                        functionName = requireNotNull(it[coreModuleFunctions.name]),
                        moduleName = requireNotNull(it[coreModules.name]),
                        moduleApi = requireNotNull(it[coreModules.api]),
                        perm = requireNotNull(it[perm]),
                    )
                }
        }

    }

}