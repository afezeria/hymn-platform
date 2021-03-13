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

    fun selectByFieldId(
        fieldId: String,
    ): MutableList<BusinessCodeRef> {
        return select({ it.fieldId eq fieldId })
    }

    private val byTriggers = CoreBizObjectTriggers("by_triggers")
    private val byTriggerObjects = CoreBizObjects("by_trigger_objects")
    private val byInterfaces = CoreCustomInterfaces("by_interfaces")
    private val byCustomFunctions = CoreCustomFunctions("by_functions")
    private val objects = CoreBizObjects()
    private val fields = CoreBizObjectFields()
    private val customFunctions = CoreCustomFunctions()
    private val columns = listOf(
        table.id,
        table.byTriggerId,
        byTriggers.api,
        byTriggerObjects.id,
        byTriggerObjects.api,
        byTriggerObjects.name,
        table.byInterfaceId,
        byInterfaces.name,
        byInterfaces.api,
        table.byCustomFunctionId,
        byCustomFunctions.api,
        table.bizObjectId,
        objects.api,
        objects.name,
        table.fieldId,
        fields.api,
        fields.name,
        table.customFunctionId,
        customFunctions.api,
    )

    fun pageSelectView(
        byTriggerId: String?,
        byInterfaceId: String?,
        byCustomFunctionId: String?,
        bizObjectId: String?,
        fieldId: String?,
        customFunctionId: String?,
        offset: Int,
        limit: Int
    ): MutableList<BusinessCodeRefListView> {
        val expr: ColumnDeclaring<Boolean>? = table.let {
            when {
                byTriggerId != null -> it.byTriggerId eq byTriggerId
                byInterfaceId != null -> it.byInterfaceId eq byInterfaceId
                byCustomFunctionId != null -> it.byCustomFunctionId eq byCustomFunctionId
                bizObjectId != null -> it.bizObjectId eq bizObjectId
                fieldId != null -> it.fieldId eq fieldId
                customFunctionId != null -> it.customFunctionId eq customFunctionId
                else -> null
            }
        }
        return databaseService.db().from(table)
            .leftJoin(byTriggers, byTriggers.id eq table.byTriggerId)
            .leftJoin(byTriggerObjects, byTriggerObjects.id eq byTriggers.bizObjectId)
            .leftJoin(byInterfaces, byInterfaces.id eq table.byInterfaceId)
            .leftJoin(byCustomFunctions, byCustomFunctions.id eq table.byCustomFunctionId)
            .leftJoin(objects, objects.id eq table.bizObjectId)
            .leftJoin(fields, fields.id eq table.fieldId)
            .leftJoin(customFunctions, customFunctions.id eq table.customFunctionId)
            .select(columns).run {
                expr?.let { where(expr) } ?: this
            }
            .limit(offset, limit)
            .mapTo(ArrayList()) {
                BusinessCodeRefListView(
                    id = requireNotNull(it[table.id]),
                    byTriggerId = it[table.byTriggerId],
                    byTriggerApi = it[byTriggers.api],
                    byTriggerObjectId = it[byTriggerObjects.id],
                    byTriggerObjectApi = it[byTriggerObjects.api],
                    byTriggerObjectName = it[byTriggerObjects.name],
                    byInterfaceId = it[table.byInterfaceId],
                    byInterfaceName = it[byInterfaces.name],
                    byInterfaceApi = it[byInterfaces.api],
                    byCustomFunctionId = it[table.byCustomFunctionId],
                    byCustomFunctionApi = it[byCustomFunctions.api],
                    bizObjectId = it[table.bizObjectId],
                    bizObjectApi = it[objects.api],
                    bizObjectName = it[objects.name],
                    fieldId = it[table.fieldId],
                    fieldApi = it[fields.api],
                    fieldName = it[fields.name],
                    customFunctionId = it[table.customFunctionId],
                    customFunctionApi = it[customFunctions.api],
                )
            }
    }


}