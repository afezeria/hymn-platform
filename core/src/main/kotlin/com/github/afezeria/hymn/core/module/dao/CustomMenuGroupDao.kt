package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.CustomMenuGroup
import com.github.afezeria.hymn.core.module.table.CoreCustomMenuGroup
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CustomMenuGroupDao(
    databaseService: DatabaseService
) : AbstractDao<CustomMenuGroup, CoreCustomMenuGroup>(
    table = CoreCustomMenuGroup(),
    databaseService = databaseService
) {

    fun selectByAccountId(
        accountId: String,
    ): MutableList<CustomMenuGroup> {
        return select({ it.accountId eq accountId })
    }
}