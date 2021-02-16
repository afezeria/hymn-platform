package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.SharedCode
import github.afezeria.hymn.core.module.table.CoreSharedCodes
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class SharedCodeDao(
    databaseService: DatabaseService
) : AbstractDao<SharedCode, CoreSharedCodes>(
    table = CoreSharedCodes(),
    databaseService = databaseService
) {


    fun selectByApi(
        api: String,
    ): SharedCode? {
        return singleRowSelect({ it.api eq api })
    }


}