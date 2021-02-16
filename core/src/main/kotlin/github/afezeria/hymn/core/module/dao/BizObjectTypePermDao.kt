package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import github.afezeria.hymn.core.module.table.CoreBizObjectTypePerms
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTypePermDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectTypePerm, CoreBizObjectTypePerms>(
    table = CoreBizObjectTypePerms(),
    databaseService = databaseService
) {


    fun selectByRoleIdAndTypeId(
        roleId: String,
        typeId: String,
    ): BizObjectTypePerm? {
        return singleRowSelect(listOf(table.roleId eq roleId, table.typeId eq typeId))
    }

    fun selectByTypeId(
        typeId: String,
    ): MutableList<BizObjectTypePerm> {
        return select({ it.typeId eq typeId })
    }



}