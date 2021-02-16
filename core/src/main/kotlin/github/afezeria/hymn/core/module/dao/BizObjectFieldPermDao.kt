package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import github.afezeria.hymn.core.module.table.CoreBizObjectFieldPerms
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectFieldPermDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectFieldPerm, CoreBizObjectFieldPerms>(
    table = CoreBizObjectFieldPerms(),
    databaseService = databaseService
) {

    fun selectByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm? {
        return singleRowSelect(listOf(table.roleId eq roleId, table.fieldId eq fieldId))
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<BizObjectFieldPerm> {
        return select({ table.roleId eq roleId })
    }

    fun selectByFieldId(
        fieldId: String,
    ): MutableList<BizObjectFieldPerm> {
        return select({ table.fieldId eq fieldId })
    }



}