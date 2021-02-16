package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectType
import github.afezeria.hymn.core.module.table.CoreBizObjectTypes
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTypeDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectType, CoreBizObjectTypes>(
    table = CoreBizObjectTypes(),
    databaseService = databaseService
) {


    fun selectByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectType? {
        return singleRowSelect(listOf(table.bizObjectId eq bizObjectId, table.name eq name))
    }


}