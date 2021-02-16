package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectLayout
import github.afezeria.hymn.core.module.table.CoreBizObjectLayouts
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectLayoutDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectLayout, CoreBizObjectLayouts>(
    table = CoreBizObjectLayouts(),
    databaseService = databaseService
) {


    fun selectByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectLayout? {
        return singleRowSelect(listOf(table.bizObjectId eq bizObjectId, table.name eq name))
    }


}