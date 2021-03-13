package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.AccountMenuLayout
import com.github.afezeria.hymn.core.module.table.CoreAccountMenuLayouts
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