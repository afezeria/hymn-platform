package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.CustomPage
import com.github.afezeria.hymn.core.module.table.CoreCustomPages
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CustomPageDao(
    databaseService: DatabaseService
) : AbstractDao<CustomPage, CoreCustomPages>(
    table = CoreCustomPages(),
    databaseService = databaseService
) {


    fun selectByApi(
        api: String,
    ): CustomPage? {
        return singleRowSelect({ it.api eq api })
    }


}