package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout
import github.afezeria.hymn.core.module.table.CoreBizObjectTypeLayouts
import org.ktorm.dsl.eq
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



}