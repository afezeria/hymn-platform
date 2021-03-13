package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.BizObjectTypeLayout
import com.github.afezeria.hymn.core.module.table.*
import com.github.afezeria.hymn.core.module.view.BizObjectTypeLayoutListView
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

    fun selectView(whereExpr: (CoreBizObjectTypeLayouts) -> ColumnDeclaring<Boolean>): MutableList<BizObjectTypeLayoutListView> {
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
                        whereExpr(this)
                }
                .mapTo(ArrayList()) {
                    BizObjectTypeLayoutListView(
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