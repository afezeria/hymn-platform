package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.CustomInterface
import github.afezeria.hymn.core.module.table.CoreCustomInterfaces
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CustomInterfaceDao(
    databaseService: DatabaseService
) : AbstractDao<CustomInterface, CoreCustomInterfaces>(
    table = CoreCustomInterfaces(),
    databaseService = databaseService
) {


    fun selectByApi(
        api: String,
    ): CustomInterface? {
        return singleRowSelect({ it.api eq api })
    }


}