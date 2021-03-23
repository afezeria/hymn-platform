package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.CustomApi
import com.github.afezeria.hymn.core.module.table.CoreCustomApi
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CustomApiDao(
    databaseService: DatabaseService
) : AbstractDao<CustomApi, CoreCustomApi>(
    table = CoreCustomApi(),
    databaseService = databaseService
) {


    fun selectByApi(
        api: String,
    ): CustomApi? {
        return singleRowSelect({ it.api eq api })
    }


}