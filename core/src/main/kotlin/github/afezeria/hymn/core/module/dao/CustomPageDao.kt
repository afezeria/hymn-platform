package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.CustomPage
import github.afezeria.hymn.core.module.table.CoreCustomPages
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