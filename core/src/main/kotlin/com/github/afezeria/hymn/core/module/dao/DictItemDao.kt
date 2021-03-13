package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.DictItem
import com.github.afezeria.hymn.core.module.table.CoreDictItems
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class DictItemDao(
    databaseService: DatabaseService
) : AbstractDao<DictItem, CoreDictItems>(
    table = CoreDictItems(),
    databaseService = databaseService
) {


    fun selectByDictIdAndCode(
        dictId: String,
        code: String,
    ): DictItem? {
        return singleRowSelect(listOf(table.dictId eq dictId, table.code eq code))
    }

    fun selectByDictId(
        dictId: String,
    ): MutableList<DictItem> {
        return select({ it.dictId eq dictId })
    }


}