package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.Role
import github.afezeria.hymn.core.module.table.CoreRoles
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class RoleDao(
    databaseService: DatabaseService
) : AbstractDao<Role, CoreRoles>(
    table = CoreRoles(),
    databaseService = databaseService
)