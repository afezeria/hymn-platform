package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectTrigger
import github.afezeria.hymn.core.module.table.CoreBizObjectTriggers
import github.afezeria.hymn.core.module.table.CoreBizObjects
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTriggerDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectTrigger, CoreBizObjectTriggers>(
    table = CoreBizObjectTriggers(),
    databaseService = databaseService
) {

    val bizObjects = CoreBizObjects()

    fun selectByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectTrigger? {
        return singleRowSelect(listOf(table.bizObjectId eq bizObjectId, table.api eq api))
    }

    fun selectAvailableTypeWithLock(
        whereExpr: ((CoreBizObjectTriggers) -> ColumnDeclaring<Boolean>)
    ): MutableList<BizObjectTrigger> {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.bizObjectId)
            .select(table.columns)
            .where { (bizObjects.active eq true) and whereExpr(table) }
            .forUpdate()
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectAvailableType(
        whereExpr: ((CoreBizObjectTriggers) -> ColumnDeclaring<Boolean>)
    ): MutableList<BizObjectTrigger> {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.bizObjectId)
            .select(table.columns)
            .where { (bizObjects.active eq true) and whereExpr(table) }
            .mapTo(ArrayList()) { table.createEntity(it) }
    }


}