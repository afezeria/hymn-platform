package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dto.ButtonPermDto
import github.afezeria.hymn.core.module.entity.ButtonPerm
import github.afezeria.hymn.core.module.table.CoreBizObjects
import github.afezeria.hymn.core.module.table.CoreButtonPerms
import github.afezeria.hymn.core.module.table.CoreCustomButtons
import github.afezeria.hymn.core.module.table.CoreRoles
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class ButtonPermDao(
    databaseService: DatabaseService
) : AbstractDao<ButtonPerm, CoreButtonPerms>(
    table = CoreButtonPerms(),
    databaseService = databaseService
) {


    fun selectByRoleIdAndButtonId(
        roleId: String,
        buttonId: String,
    ): ButtonPerm? {
        return singleRowSelect(listOf(table.roleId eq roleId, table.buttonId eq buttonId))
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<ButtonPerm> {
        return select({ it.roleId eq roleId })
    }

    fun selectByButtonId(
        buttonId: String,
    ): MutableList<ButtonPerm> {
        return select({ it.buttonId eq buttonId })
    }

    private val bizObjects = CoreBizObjects()
    private val buttons = CoreCustomButtons()
    private val roles = CoreRoles()

    fun selectDto(whereExpr: (CoreButtonPerms, CoreCustomButtons) -> ColumnDeclaring<Boolean>): MutableList<ButtonPermDto> {
        return table.run {
            databaseService.db().from(this)
                .innerJoin(buttons, buttons.id eq buttonId)
                .innerJoin(roles, roles.id eq roleId)
                .leftJoin(bizObjects, bizObjects.id eq buttons.bizObjectId)
                .select(
                    roleId,
                    buttonId,
                    visible,
                    roles.name,
                    buttons.name,
                    bizObjects.id,
                    bizObjects.name
                )
                .where {
                    (bizObjects.id.isNull() or (bizObjects.active eq true)) and
                        whereExpr(this, buttons)
                }
                .mapTo(ArrayList()) {
                    ButtonPermDto(
                        roleId = requireNotNull(it[roleId]),
                        buttonId = requireNotNull(it[buttonId]),
                        visible = requireNotNull(it[visible])
                    ).apply {
                        roleName = it[roles.name]
                        buttonName = it[buttons.name]
                        bizObjectId = it[bizObjects.id]
                        bizObjectName = it[bizObjects.name]
                    }
                }
        }

    }

}