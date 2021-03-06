package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import com.github.afezeria.hymn.core.module.table.CoreBizObjectMappingItems
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectMappingItemDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectMappingItem, CoreBizObjectMappingItems>(
    table = CoreBizObjectMappingItems(),
    databaseService = databaseService
) {
    fun batchSave(entityList: List<BizObjectMappingItem>): Int {
        return bulkInsertOrUpdate(
            entityList,
            table.mappingId,
            table.sourceFieldId,
            table.targetFieldId
        )
    }
}