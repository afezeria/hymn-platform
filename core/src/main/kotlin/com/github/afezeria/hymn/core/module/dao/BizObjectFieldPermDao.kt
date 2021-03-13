package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import com.github.afezeria.hymn.core.module.table.CoreBizObjectFieldPerms
import com.github.afezeria.hymn.core.module.table.CoreBizObjectFields
import com.github.afezeria.hymn.core.module.table.CoreBizObjects
import com.github.afezeria.hymn.core.module.table.CoreRoles
import com.github.afezeria.hymn.core.module.view.BizObjectFieldPermListView
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

    fun selectView(
        whereExpr: (CoreBizObjectFieldPerms, CoreBizObjectFields) -> ColumnDeclaring<Boolean>
    ): List<BizObjectFieldPermListView> {
        return table.run {
            databaseService.db().from(this)
                .innerJoin(bizObjectFields, bizObjectFields.id eq fieldId)
                .innerJoin(bizObjects, bizObjects.id eq bizObjectFields.bizObjectId)
                .innerJoin(roles, roles.id eq roleId)
                .select(
                    roleId,
                    fieldId,
                    pRead,
                    pEdit,
                    roles.name,
                    bizObjects.id,
                    bizObjectFields.name,
                )
                .where {
                    (bizObjects.active eq true) and
                        (bizObjectFields.active eq true) and
                        whereExpr(this, bizObjectFields)
                }
                .mapTo(ArrayList()) {
                    BizObjectFieldPermListView(
                        roleId = requireNotNull(it[roleId]),
                        fieldId = requireNotNull(it[fieldId]),
                        pRead = requireNotNull(it[pRead]),
                        pEdit = requireNotNull(it[pEdit]),
                        roleName = requireNotNull(it[roles.name]),
                        bizObjectId = requireNotNull(it[bizObjects.id]),
                        fieldName = requireNotNull(it[bizObjectFields.name]),
                    )
                }
        }
    }


}