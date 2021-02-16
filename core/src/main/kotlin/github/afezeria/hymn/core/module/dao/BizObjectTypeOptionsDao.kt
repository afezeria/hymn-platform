package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectTypeOptions
import github.afezeria.hymn.core.module.table.CoreBizObjectTypeOptionss
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTypeOptionsDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectTypeOptions, CoreBizObjectTypeOptionss>(
    table = CoreBizObjectTypeOptionss(),
    databaseService = databaseService
) {


    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectTypeOptions> {
        return select({ it.bizObjectId eq bizObjectId })
    }

    fun selectByTypeId(
        typeId: String,
    ): MutableList<BizObjectTypeOptions> {
        return select({ it.typeId eq typeId })
    }


}