package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.Org
import github.afezeria.hymn.core.module.table.CoreOrgs
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class OrgDao(
    databaseService: DatabaseService
) : AbstractDao<Org, CoreOrgs>(
    table = CoreOrgs(),
    databaseService = databaseService
) {


    fun selectByParentId(
        parentId: String,
    ): MutableList<Org> {
        return select({ it.parentId eq parentId })
    }


}