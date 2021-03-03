package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectLayout
import github.afezeria.hymn.core.module.table.CoreBizObjectLayouts
import github.afezeria.hymn.core.module.table.CoreBizObjects
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectLayoutDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectLayout, CoreBizObjectLayouts>(
    table = CoreBizObjectLayouts(),
    databaseService = databaseService
) {

    val bizObjects = CoreBizObjects()

    fun selectAvailableLayout(
        whereExpr: ((CoreBizObjectLayouts) -> ColumnDeclaring<Boolean>)
    ): List<BizObjectLayout> {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.bizObjectId)
            .select(table.columns)
            .where { (bizObjects.active eq true) and whereExpr(table) }
            .mapTo(ArrayList()) { table.createEntity(it) }
    }


}