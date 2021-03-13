package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.BizObject
import com.github.afezeria.hymn.core.module.table.CoreBizObjects
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
)