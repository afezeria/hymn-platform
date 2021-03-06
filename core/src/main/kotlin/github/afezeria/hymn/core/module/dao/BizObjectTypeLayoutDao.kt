package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout
import github.afezeria.hymn.core.module.table.*
import github.afezeria.hymn.core.module.view.ObjectTypeLayoutListView
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTypeLayoutDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectTypeLayout, CoreBizObjectTypeLayouts>(
    table = CoreBizObjectTypeLayouts(),
    databaseService = databaseService
) {


    fun selectByRoleIdAndTypeIdAndLayoutId(
        roleId: String,
        typeId: String,
        layoutId: String,
    ): BizObjectTypeLayout? {
        return singleRowSelect(
            listOf(
                table.roleId eq roleId,
                table.typeId eq typeId,
                table.layoutId eq layoutId
            )
        )
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<BizObjectTypeLayout> {
        return select({ table.roleId eq roleId })
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectTypeLayout> {
        return select({ it.bizObjectId eq bizObjectId })
    }

    private val types = CoreBizObjectTypes()
    private val roles = CoreRoles()
    private val bizObjects = CoreBizObjects()
    private val layouts = CoreBizObjectLayouts()

    fun selectView(whereExpr: (CoreBizObjectTypeLayouts) -> ColumnDeclaring<Boolean>): MutableList<ObjectTypeLayoutListView> {
        return table.run {
            databaseService.db().from(this)
                .innerJoin(roles, roles.id eq roleId)
                .innerJoin(types, types.id eq typeId)
                .innerJoin(layouts, layouts.id eq layoutId)
                .innerJoin(bizObjects, bizObjects.id eq table.bizObjectId)
                .select(
                    roleId,
                    typeId,
                    layoutId,
                    roles.name,
                    types.name,
                    layouts.name,
                )
                .where {
                    (bizObjects.active eq true) and
                        whereExpr(this, types)
                }
                .mapTo(ArrayList()) {
                    ObjectTypeLayoutListView(
                        roleId = requireNotNull(it[roleId]),
                        typeId = requireNotNull(it[typeId]),
                        layoutId = requireNotNull(it[layoutId]),
                        roleName = requireNotNull(it[roles.name]),
                        layoutName = requireNotNull(it[layouts.name]),
                        typeName = requireNotNull(it[types.name]),
                    )
                }
        }
    }


}