package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BizObjectTypeFieldOption
import github.afezeria.hymn.core.module.table.CoreBizObjectFields
import github.afezeria.hymn.core.module.table.CoreBizObjectTypeFieldOptions
import github.afezeria.hymn.core.module.table.CoreBizObjects
import github.afezeria.hymn.core.module.table.CoreDictItems
import github.afezeria.hymn.core.module.view.BizObjectTypeFieldOptionListView
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTypeFieldOptionDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectTypeFieldOption, CoreBizObjectTypeFieldOptions>(
    table = CoreBizObjectTypeFieldOptions(),
    databaseService = databaseService
) {


    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectTypeFieldOption> {
        return select({ it.bizObjectId eq bizObjectId })
    }

    fun selectByTypeId(
        typeId: String,
    ): MutableList<BizObjectTypeFieldOption> {
        return select({ it.typeId eq typeId })
    }

    val bizObjects = CoreBizObjects()
    val fields = CoreBizObjectFields()
    val dictItems = CoreDictItems()
    fun selectView(
        expr: (CoreBizObjectTypeFieldOptions) -> ColumnDeclaring<Boolean>
    ): List<BizObjectTypeFieldOptionListView> {
        return table.run {
            databaseService.db().from(this)
                .innerJoin(bizObjects, bizObjects.id eq bizObjectId)
                .innerJoin(fields, fields.id eq fieldId)
                .innerJoin(dictItems, dictItems.id eq dictItemId)
                .select(
                    bizObjectId,
                    typeId,
                    fieldId,
                    fields.name,
                    dictItemId,
                    dictItems.name,
                    dictItems.code,
                )
                .where {
                    (bizObjects.active eq true) and
                        expr(this)
                }
                .mapTo(ArrayList()) {
                    BizObjectTypeFieldOptionListView(
                        bizObjectId = requireNotNull(it[bizObjectId]),
                        typeId = requireNotNull(it[typeId]),
                        fieldId = requireNotNull(it[fieldId]),
                        fieldName = requireNotNull(it[fields.name]),
                        dictItemId = requireNotNull(it[dictItemId]),
                        dictItemName = requireNotNull(it[dictItems.name]),
                        dictItemCode = requireNotNull(it[dictItems.code]),
                    )
                }
        }
    }
}