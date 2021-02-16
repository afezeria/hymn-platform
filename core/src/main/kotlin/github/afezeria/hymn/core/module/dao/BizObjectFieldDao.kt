package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectField
import github.afezeria.hymn.core.module.table.CoreBizObjectFields
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectFieldDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectField, CoreBizObjectFields>(
    table = CoreBizObjectFields(),
    databaseService = databaseService
) {
    fun selectByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectField? {
        return singleRowSelect(listOf(table.bizObjectId eq bizObjectId, table.api eq api))
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectField> {
        return select({ it.bizObjectId eq bizObjectId })
    }


}