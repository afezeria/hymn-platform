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

    fun selectByRefIdAndActiveIsTrue(refObjectId: String): MutableList<BizObjectField> {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.refId)
            .select(table.columns)
            //对象停用时不能有关联到该对象的启用的字段，所以这里不需要判断被关联对象的启用状态
            .where { (table.refId eq refObjectId) and (table.active eq true) }
            .mapTo(ArrayList()) { table.createEntity(it) }
    }


}