package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.Config
import github.afezeria.hymn.core.module.table.CoreConfigs
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class ConfigDao(
    databaseService: DatabaseService
) : AbstractDao<Config, CoreConfigs>(
    table = CoreConfigs(),
    databaseService = databaseService
) {

    fun selectByKey(key: String): Config? {
        return singleRowSelect({ it.key eq key })
    }


    fun selectByKeyPattern(
        key: String,
    ): MutableList<Config> {
        return select({ it.key like "%$key%" })
    }


}