package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.DictItem
import github.afezeria.hymn.core.module.table.CoreDictItems
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