package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectType
import github.afezeria.hymn.core.module.table.CoreBizObjectTypes
import github.afezeria.hymn.core.module.table.CoreBizObjects
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTypeDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectType, CoreBizObjectTypes>(
    table = CoreBizObjectTypes(),
    databaseService = databaseService
) {

    val bizObjects = CoreBizObjects()

    fun selectByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectType? {
        return singleRowSelect(listOf(table.bizObjectId eq bizObjectId, table.name eq name))
    }

    fun selectAvailableType(
        whereExpr: ((CoreBizObjectTypes) -> ColumnDeclaring<Boolean>)
    ): List<BizObjectType> {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.bizObjectId)
            .select(table.columns)
            .where { (bizObjects.active eq true) and whereExpr(table) }
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectAvailableTypeByBizObjectId(bizObjectId: String): List<BizObjectType> {
        return databaseService.db().from(bizObjects)
            .leftJoin(table, bizObjects.id eq table.bizObjectId)
            .select(table.columns)
            //对象停用时不能有关联到该对象的启用的字段，所以这里不需要判断被关联对象的启用状态
            .where { bizObjects.active eq true }
            .mapTo(ArrayList()) { table.createEntity(it) }
    }


}