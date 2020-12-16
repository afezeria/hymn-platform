package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.BizObjectTypeOptions

/**
 * @author afezeria
 */
class CoreBizObjectTypeOptionss(alias: String? = null) :
    BaseTable<BizObjectTypeOptions>("core_biz_object_type_options", schema = "hymn", alias = alias) {

    val bizObjectId = varchar("biz_object_id")
    val typeId = varchar("type_id")
    val fieldId = varchar("field_id")
    val dictItemId = varchar("dict_item_id")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectTypeOptions(
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field BizObjectTypeOptions.bizObjectId should not be null" },
        typeId = requireNotNull(row[this.typeId]) { "field BizObjectTypeOptions.typeId should not be null" },
        fieldId = requireNotNull(row[this.fieldId]) { "field BizObjectTypeOptions.fieldId should not be null" },
        dictItemId = requireNotNull(row[this.dictItemId]) { "field BizObjectTypeOptions.dictItemId should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectTypeOptions.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field BizObjectTypeOptions.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field BizObjectTypeOptions.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field BizObjectTypeOptions.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field BizObjectTypeOptions.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field BizObjectTypeOptions.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field BizObjectTypeOptions.modifyDate should not be null" }
    }
}
