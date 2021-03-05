package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dto.BizObjectMappingDto
import github.afezeria.hymn.core.module.entity.BizObjectMapping
import github.afezeria.hymn.core.module.table.CoreBizObjectMappings
import github.afezeria.hymn.core.module.table.CoreBizObjectTypes
import github.afezeria.hymn.core.module.table.CoreBizObjects
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectMappingDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectMapping, CoreBizObjectMappings>(
    table = CoreBizObjectMappings(),
    databaseService = databaseService
) {

    private val sourceObjects = CoreBizObjects("source_obj")
    private val targetObjects = CoreBizObjects("target_obj")
    private val sourceTypes = CoreBizObjectTypes("source_type")
    private val targetTypes = CoreBizObjectTypes("target_type")

    fun selectBySourceBizObjectId(
        sourceBizObjectId: String,
    ): MutableList<BizObjectMapping> {
        return select({ table.sourceBizObjectId eq sourceBizObjectId })
    }

    val dtoColumns = listOf(
        table.id,
        table.sourceBizObjectId,
        table.sourceTypeId,
        table.targetBizObjectId,
        table.targetTypeId,
        sourceObjects.name,
        sourceTypes.name,
        targetObjects.name,
        targetTypes.name,
    )
    val baseExpr = (sourceObjects.active eq true) and
        (targetObjects.active eq true)

    fun selectDto(
        expr: ((CoreBizObjectMappings) -> ColumnDeclaring<Boolean>)?,
        offset: Int,
        limit: Int
    ): MutableList<BizObjectMappingDto> {
        return table.run {
            databaseService.db().from(table)
                .innerJoin(sourceObjects, sourceObjects.id eq sourceBizObjectId)
                .innerJoin(targetObjects, targetObjects.id eq targetBizObjectId)
                .innerJoin(
                    sourceTypes,
                    (sourceTypes.id eq sourceTypeId) and (sourceTypes.bizObjectId eq sourceBizObjectId)
                )
                .innerJoin(
                    targetTypes,
                    (targetTypes.id eq targetTypeId) and (targetTypes.bizObjectId eq targetBizObjectId)
                )
                .select(dtoColumns)
                .where {
                    if (expr == null) {
                        baseExpr
                    } else {
                        baseExpr and expr(table)
                    }
                }
                .limit(offset, limit)
                .mapTo(ArrayList()) {
                    BizObjectMappingDto(
                        sourceBizObjectId = requireNotNull(it[sourceBizObjectId]),
                        sourceTypeId = requireNotNull(it[sourceTypeId]),
                        targetBizObjectId = requireNotNull(it[targetBizObjectId]),
                        targetTypeId = requireNotNull(it[targetTypeId]),
                    ).apply {
                        id = requireNotNull(it[table.id])
                        sourceBizObjectName = requireNotNull(it[sourceObjects.name])
                        sourceTypeName = requireNotNull(it[sourceTypes.name])
                        targetBizObjectName = requireNotNull(it[targetObjects.name])
                        targetTypeName = requireNotNull(it[targetTypes.name])
                    }
                }
        }
    }


}