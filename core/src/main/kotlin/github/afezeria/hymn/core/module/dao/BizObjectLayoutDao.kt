package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectLayout
import github.afezeria.hymn.core.module.table.CoreBizObjectLayouts
import github.afezeria.hymn.core.module.table.CoreBizObjectTypeLayouts
import github.afezeria.hymn.core.module.table.CoreBizObjects
import github.afezeria.hymn.core.module.view.BizObjectLayoutListView
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

    val typeLayouts = CoreBizObjectTypeLayouts()
    fun selectByRoleIdAndTypeId(roleId: String, typeId: String): BizObjectLayout? {
        return databaseService.db().from(table)
            .innerJoin(typeLayouts, typeLayouts.layoutId eq table.id)
            .select(table.columns)
            .where { (typeLayouts.roleId eq roleId) and (typeLayouts.typeId eq typeId) }
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectView(expr: ((CoreBizObjectLayouts) -> ColumnDeclaring<Boolean>)): MutableList<BizObjectLayoutListView> {
        return table.run {
            databaseService.db().from(table)
                .innerJoin(bizObjects, bizObjects.id eq bizObjectId)
                .select(
                    id, bizObjectId, name, remark, createById,
                    createBy, modifyById, modifyBy, createDate, modifyDate,
                ).where {
                    (bizObjects.active eq true) and
                        expr(table)
                }.mapTo(ArrayList()) {
                    BizObjectLayoutListView(
                        id = requireNotNull(it[id]),
                        bizObjectId = requireNotNull(it[bizObjectId]),
                        name = requireNotNull(it[name]),
                        remark = requireNotNull(it[remark]),
                        createById = requireNotNull(it[createById]),
                        createBy = requireNotNull(it[createBy]),
                        modifyById = requireNotNull(it[modifyById]),
                        modifyBy = requireNotNull(it[modifyBy]),
                        createDate = requireNotNull(it[createDate]),
                        modifyDate = requireNotNull(it[modifyDate]),
                    )
                }
        }
    }


}