package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import github.afezeria.hymn.core.module.table.CoreBizObjectTypePerms
import github.afezeria.hymn.core.module.table.CoreBizObjectTypes
import github.afezeria.hymn.core.module.table.CoreBizObjects
import github.afezeria.hymn.core.module.table.CoreRoles
import github.afezeria.hymn.core.module.view.BizObjectTypePermListView
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
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
    private val bizObjects = CoreBizObjects()
    private val types = CoreBizObjectTypes()
    private val roles = CoreRoles()

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
            .innerJoin(types, types.id eq table.typeId)
            .select(table.columns)
            .where { (table.roleId eq roleId) eq (types.bizObjectId eq bizObjectId) }
            .mapToArray(table)
    }

    fun selectView(whereExpr: (CoreBizObjectTypePerms, CoreBizObjectTypes) -> ColumnDeclaring<Boolean>): MutableList<BizObjectTypePermListView> {
        return table.run {
            databaseService.db().from(this)
                .innerJoin(types, types.id eq typeId)
                .innerJoin(bizObjects, bizObjects.id eq types.bizObjectId)
                .innerJoin(roles, roles.id eq roleId)
                .select(
                    roleId,
                    typeId,
                    visible,
                    roles.name,
                    types.name
                )
                .where {
                    (bizObjects.active eq true) and
                        whereExpr(this, types)
                }
                .mapTo(ArrayList()) {
                    BizObjectTypePermListView(
                        roleId = requireNotNull(it[roleId]),
                        roleName = requireNotNull(it[roles.name]),
                        typeId = requireNotNull(it[typeId]),
                        typeName = requireNotNull(it[types.name]),
                        bizObjectId = requireNotNull(it[types.bizObjectId]),
                        visible = requireNotNull(it[visible]),
                    )
                }
        }

    }
}