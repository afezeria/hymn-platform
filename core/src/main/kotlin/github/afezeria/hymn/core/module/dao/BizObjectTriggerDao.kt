package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectTrigger
import github.afezeria.hymn.core.module.table.CoreBizObjectTriggers
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTriggerDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectTrigger, CoreBizObjectTriggers>(
    table = CoreBizObjectTriggers(),
    databaseService = databaseService
) {


    fun selectByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectTrigger? {
        return singleRowSelect(listOf(table.bizObjectId eq bizObjectId, table.api eq api))
    }


}