package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import github.afezeria.hymn.core.module.table.CoreBizObjectTypePerms
import github.afezeria.hymn.core.module.table.CoreBizObjectTypes
import github.afezeria.hymn.core.module.table.CoreBizObjects
import org.ktorm.dsl.*
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
    val bizObjects = CoreBizObjects()
    val bizObjectTypes = CoreBizObjectTypes()

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

    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): MutableList<BizObjectTypePerm> {
        return databaseService.db().from(table)
            .innerJoin(bizObjectTypes, bizObjectTypes.id eq table.typeId)
            .select(table.columns)
            .where { (table.roleId eq roleId) eq (bizObjectTypes.bizObjectId eq bizObjectId) }
            .mapToArray(table)
    }
}