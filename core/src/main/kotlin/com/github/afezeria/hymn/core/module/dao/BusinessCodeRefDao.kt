package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.BusinessCodeRef
import com.github.afezeria.hymn.core.module.table.*
import com.github.afezeria.hymn.core.module.view.BusinessCodeRefListView
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BusinessCodeRefDao(
    databaseService: DatabaseService
) : AbstractDao<BusinessCodeRef, CoreBusinessCodeRefs>(
    table = CoreBusinessCodeRefs(),
    databaseService = databaseService
) {

    fun save(entityList: List<BusinessCodeRef>): Int {
        return bulkInsertOrUpdate(
            entityList,
            table.byObjectId,
            table.byTriggerId,
            table.byApiId,
            table.byFunctionId,
            table.refObjectId,
            table.refFieldId,
            table.refFunctionId
        )
    }

    fun selectBaseFunctionIds(condition: ((CoreBusinessCodeRefs) -> ColumnDeclaring<Boolean>)): MutableList<String> {
        return select(
            listOf(table.refFunctionId),
            { condition(table) and table.refFunctionId.isNotNull() })
            .mapTo(mutableListOf()) { requireNotNull(it[table.refFunctionId.name]) as String }
    }

    fun selectByFieldId(
        fieldId: String,
    ): MutableList<BusinessCodeRef> {
        return select({ it.refFieldId eq fieldId })
    }


    private val byObjects = CoreBizObjects("by_objects")
    private val byTriggers = CoreBizObjectTriggers("by_triggers")
    private val byTriggerObjects = CoreBizObjects("by_trigger_objects")
    private val byInterfaces = CoreCustomApi("by_interfaces")
    private val byCustomFunctions = CoreCustomFunctions("by_functions")
    private val objects = CoreBizObjects()
    private val fields = CoreBizObjectFields()
    private val customFunctions = CoreCustomFunctions()
    private val columns = listOf(
        table.id,
        table.byObjectId,
        byObjects.api,
        byObjects.name,
        table.byTriggerId,
        byTriggers.api,
        byTriggerObjects.id,
        byTriggerObjects.api,
        byTriggerObjects.name,
        table.byApiId,
        byInterfaces.name,
        byInterfaces.api,
        table.byFunctionId,
        byCustomFunctions.api,
        table.refObjectId,
        objects.api,
        objects.name,
        table.refFieldId,
        fields.api,
        fields.name,
        table.refFunctionId,
        customFunctions.api,
    )

    fun pageSelectView(
        byObjectId: String?,
        byTriggerId: String?,
        byApiId: String?,
        byFunctionId: String?,
        refObjectId: String?,
        refFieldId: String?,
        refFunctionId: String?,
        offset: Int,
        limit: Int
    ): MutableList<BusinessCodeRefListView> {
        val expr: ColumnDeclaring<Boolean>? = table.let {
            when {
                byObjectId != null -> it.byObjectId eq byObjectId
                byTriggerId != null -> it.byTriggerId eq byTriggerId
                byApiId != null -> it.byApiId eq byApiId
                byFunctionId != null -> it.byFunctionId eq byFunctionId
                refObjectId != null -> it.refObjectId eq refObjectId
                refFieldId != null -> it.refFieldId eq refFieldId
                refFunctionId != null -> it.refFunctionId eq refFunctionId
                else -> null
            }
        }
        return databaseService.db().from(table)
            .leftJoin(byObjects, byObjects.id eq table.byObjectId)
            .leftJoin(byTriggers, byTriggers.id eq table.byTriggerId)
            .leftJoin(byTriggerObjects, byTriggerObjects.id eq byTriggers.bizObjectId)
            .leftJoin(byInterfaces, byInterfaces.id eq table.byApiId)
            .leftJoin(byCustomFunctions, byCustomFunctions.id eq table.byFunctionId)
            .leftJoin(objects, objects.id eq table.refObjectId)
            .leftJoin(fields, fields.id eq table.refFieldId)
            .leftJoin(customFunctions, customFunctions.id eq table.refFunctionId)
            .select(columns).run {
                expr?.let { where(expr) } ?: this
            }
            .limit(offset, limit)
            .mapTo(ArrayList()) {
                BusinessCodeRefListView(
                    id = requireNotNull(it[table.id]),
                    byObjectId = it[table.byObjectId],
                    byObjectApi = it[byObjects.api],
                    byObjectName = it[byObjects.name],
                    byTriggerId = it[table.byTriggerId],
                    byTriggerApi = it[byTriggers.api],
                    byTriggerObjectId = it[byTriggerObjects.id],
                    byTriggerObjectApi = it[byTriggerObjects.api],
                    byTriggerObjectName = it[byTriggerObjects.name],
                    byInterfaceId = it[table.byApiId],
                    byInterfaceName = it[byInterfaces.name],
                    byInterfaceApi = it[byInterfaces.api],
                    byFunctionId = it[table.byFunctionId],
                    byFunctionApi = it[byCustomFunctions.api],
                    refObjectId = it[table.refObjectId],
                    refObjectApi = it[objects.api],
                    refObjectName = it[objects.name],
                    refFieldId = it[table.refFieldId],
                    refFieldApi = it[fields.api],
                    refFieldName = it[fields.name],
                    refFunctionId = it[table.refFunctionId],
                    refFunctionApi = it[customFunctions.api],
                )
            }
    }


}