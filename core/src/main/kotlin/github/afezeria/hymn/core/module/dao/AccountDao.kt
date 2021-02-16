package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.Account
import github.afezeria.hymn.core.module.table.CoreAccounts
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class AccountDao(
    databaseService: DatabaseService
) : AbstractDao<Account, CoreAccounts>(table = CoreAccounts(), databaseService) {


    fun selectByLeaderId(
        leaderId: String,
    ): MutableList<Account> {
        return select({ it.leaderId eq leaderId })
    }

    fun selectByOrgId(
        orgId: String,
    ): MutableList<Account> {
        return select({ it.orgId eq orgId })
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<Account> {
        return select({ it.roleId eq roleId })
    }
}