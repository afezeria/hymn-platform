package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.ButtonPerm
import github.afezeria.hymn.core.module.table.CoreButtonPerms
import org.ktorm.dsl.eq
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


}