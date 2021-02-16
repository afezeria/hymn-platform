package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.CustomMenuItem
import github.afezeria.hymn.core.module.table.CoreCustomMenuItems
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