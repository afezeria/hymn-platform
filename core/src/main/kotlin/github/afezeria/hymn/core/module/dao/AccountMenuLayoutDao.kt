package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.AccountMenuLayout
import github.afezeria.hymn.core.module.table.CoreAccountMenuLayouts
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class AccountMenuLayoutDao(
    databaseService: DatabaseService
) : AbstractDao<AccountMenuLayout, CoreAccountMenuLayouts>(
    table = CoreAccountMenuLayouts(),
    databaseService = databaseService
) {

    fun selectByAccountId(
        accountId: String,
    ): MutableList<AccountMenuLayout> {
        return select({ it.accountId eq accountId })
    }
}