package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectMapping
import github.afezeria.hymn.core.module.table.CoreBizObjectMappings
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectMappingDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectMapping, CoreBizObjectMappings>(
    table = CoreBizObjectMappings(),
    databaseService = databaseService
) {


    fun selectBySourceBizObjectId(
        sourceBizObjectId: String,
    ): MutableList<BizObjectMapping> {
        return select({ table.sourceBizObjectId eq sourceBizObjectId })
    }


}