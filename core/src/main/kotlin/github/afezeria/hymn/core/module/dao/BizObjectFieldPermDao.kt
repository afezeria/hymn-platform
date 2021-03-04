package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import github.afezeria.hymn.core.module.table.CoreBizObjectFieldPerms
import github.afezeria.hymn.core.module.table.CoreBizObjectFields
import github.afezeria.hymn.core.module.table.CoreBizObjects
import github.afezeria.hymn.core.module.table.CoreRoles
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
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

    private val bizObjects = CoreBizObjects()
    private val bizObjectFields = CoreBizObjectFields()
    private val roles = CoreRoles()

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

    fun selectDto(
        whereExpr: (CoreBizObjectFieldPerms, CoreBizObjectFields) -> ColumnDeclaring<Boolean>
    ): List<BizObjectFieldPermDto> {
        return table.run {
            databaseService.db().from(this)
                .innerJoin(bizObjects, bizObjects.id eq bizObjectId)
                .innerJoin(bizObjectFields, bizObjectFields.id eq fieldId)
                .innerJoin(roles, roles.id eq roleId)
                .select(
                    roleId,
                    bizObjectId,
                    fieldId,
                    pRead,
                    pEdit,
                    roles.name,
                    bizObjectFields.name
                )
                .where {
                    (bizObjects.active eq true) and
                        (bizObjectFields.active eq true) and
                        whereExpr(this, bizObjectFields)
                }
                .mapTo(ArrayList()) {
                    BizObjectFieldPermDto(
                        roleId = requireNotNull(it[roleId]),
                        bizObjectId = requireNotNull(it[bizObjectId]),
                        fieldId = requireNotNull(it[fieldId]),
                        pRead = requireNotNull(it[pRead]),
                        pEdit = requireNotNull(it[pEdit])
                    ).apply {
                        roleName = requireNotNull(it[roles.name])
                        fieldName = requireNotNull(it[bizObjectFields.name])
                    }
                }
        }
    }


}