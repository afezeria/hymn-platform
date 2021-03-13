package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.CustomButton
import com.github.afezeria.hymn.core.module.table.CoreCustomButtons
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CustomButtonDao(
    databaseService: DatabaseService
) : AbstractDao<CustomButton, CoreCustomButtons>(
    table = CoreCustomButtons(),
    databaseService = databaseService
) {


    fun selectByApi(
        api: String,
    ): CustomButton? {
        return singleRowSelect({ it.api eq api })
    }


}