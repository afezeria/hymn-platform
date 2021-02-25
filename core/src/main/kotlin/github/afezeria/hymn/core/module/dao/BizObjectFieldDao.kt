package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectField
import github.afezeria.hymn.core.module.table.CoreBizObjectFields
import github.afezeria.hymn.core.module.table.CoreBizObjects
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectFieldDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectField, CoreBizObjectFields>(
    table = CoreBizObjectFields(),
    databaseService = databaseService
) {
    val bizObjects = CoreBizObjects()

    fun selectByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectField? {
        return singleRowSelect(listOf(table.bizObjectId eq bizObjectId, table.api eq api))
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectField> {
        return select({ it.bizObjectId eq bizObjectId })
    }

    fun selectByRefIdAndActiveIsTrue(id: String): MutableList<BizObjectField> {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.bizObjectId)
            .select(table.columns)
            .where { (table.refId eq id) and (table.active eq true) and (bizObjects.active eq true) }
            .mapTo(ArrayList()) { table.createEntity(it) }
    }


}