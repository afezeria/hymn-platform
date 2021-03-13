package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.CustomFunction
import com.github.afezeria.hymn.core.module.table.CoreCustomFunctions
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CustomFunctionDao(
    databaseService: DatabaseService
) : AbstractDao<CustomFunction, CoreCustomFunctions>(
    table = CoreCustomFunctions(),
    databaseService = databaseService
) {


    fun selectByApi(
        api: String,
    ): CustomFunction? {
        return singleRowSelect({ it.api eq api })
    }


}