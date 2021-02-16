package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.CustomComponent
import github.afezeria.hymn.core.module.table.CoreCustomComponents
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CustomComponentDao(
    databaseService: DatabaseService
) : AbstractDao<CustomComponent, CoreCustomComponents>(
    table = CoreCustomComponents(),
    databaseService = databaseService
) {
    fun selectByApi(
        api: String,
    ): CustomComponent? {
        return singleRowSelect({ it.api eq api })
    }
}