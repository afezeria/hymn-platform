package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.CustomMenuItem
import com.github.afezeria.hymn.core.module.table.CoreCustomMenuItems
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CustomMenuItemDao(
    databaseService: DatabaseService
) : AbstractDao<CustomMenuItem, CoreCustomMenuItems>(
    table = CoreCustomMenuItems(),
    databaseService = databaseService
)