package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.Dict
import github.afezeria.hymn.core.module.table.CoreDicts
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class DictDao(
    databaseService: DatabaseService
) : AbstractDao<Dict, CoreDicts>(
    table = CoreDicts(),
    databaseService = databaseService
) {


    fun selectByApi(
        api: String,
    ): Dict? {
        return singleRowSelect({ it.api eq api })
    }


}