package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.AccountObjectView
import com.github.afezeria.hymn.core.module.table.CoreAccountObjectViews
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class AccountObjectViewDao(
    databaseService: DatabaseService
) : AbstractDao<AccountObjectView, CoreAccountObjectViews>(
    table = CoreAccountObjectViews(),
    databaseService = databaseService
) {

    fun selectByAccountId(
        accountId: String,
    ): MutableList<AccountObjectView> {
        return select({ it.accountId eq accountId })
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<AccountObjectView> {
        return select({ it.bizObjectId eq bizObjectId })
    }


}