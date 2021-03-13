package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.Config
import com.github.afezeria.hymn.core.module.table.CoreConfigs
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