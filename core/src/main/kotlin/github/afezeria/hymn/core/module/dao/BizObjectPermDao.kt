package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectPerm
import github.afezeria.hymn.core.module.table.CoreBizObjectPerms
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectPermDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectPerm, CoreBizObjectPerms>(
    table = CoreBizObjectPerms(),
    databaseService = databaseService
) {


    fun selectByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm? {
        return singleRowSelect(listOf(table.roleId eq roleId, table.bizObjectId eq bizObjectId))
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<BizObjectPerm> {
        return select({ table.roleId eq roleId })
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectPerm> {
        return select({ table.bizObjectId eq bizObjectId })
    }

}