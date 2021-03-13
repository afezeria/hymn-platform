package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.Role
import com.github.afezeria.hymn.core.module.table.CoreRoles
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