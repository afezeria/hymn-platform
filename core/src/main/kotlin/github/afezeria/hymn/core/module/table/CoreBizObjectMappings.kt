package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.BizObjectMapping

/**
 * @author afezeria
 */
class CoreBizObjectMappings(alias: String? = null) :
    BaseTable<BizObjectMapping>("core_biz_object_mapping", schema = "hymn", alias = alias) {

    val sourceBizObjectId = varchar("source_biz_object_id")
    val sourceTypeId = varchar("source_type_id")
    val targetBizObjectId = varchar("target_biz_object_id")
    val targetTypeId = varchar("target_type_id")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectMapping(
        sourceBizObjectId = requireNotNull(row[this.sourceBizObjectId]) { "field BizObjectMapping.sourceBizObjectId should not be null" },
        sourceTypeId = requireNotNull(row[this.sourceTypeId]) { "field BizObjectMapping.sourceTypeId should not be null" },
        targetBizObjectId = requireNotNull(row[this.targetBizObjectId]) { "field BizObjectMapping.targetBizObjectId should not be null" },
        targetTypeId = requireNotNull(row[this.targetTypeId]) { "field BizObjectMapping.targetTypeId should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectMapping.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field BizObjectMapping.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field BizObjectMapping.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field BizObjectMapping.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field BizObjectMapping.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field BizObjectMapping.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field BizObjectMapping.modifyDate should not be null" }
    }
}
