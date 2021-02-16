package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObject
import github.afezeria.hymn.core.module.table.CoreBizObjects
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectDao(
    databaseService: DatabaseService
) : AbstractDao<BizObject, CoreBizObjects>(
    table = CoreBizObjects(),
    databaseService = databaseService
) {

    fun selectByApi(
        api: String,
    ): BizObject? {
        return singleRowSelect({ it.api eq api })
    }


}