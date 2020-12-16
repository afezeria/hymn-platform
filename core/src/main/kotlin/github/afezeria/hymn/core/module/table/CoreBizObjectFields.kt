package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.BizObjectField

/**
 * @author afezeria
 */
class CoreBizObjectFields(alias: String? = null) :
    BaseTable<BizObjectField>("core_biz_object_field", schema = "hymn", alias = alias) {

    val sourceColumn = varchar("source_column")
    val bizObjectId = varchar("biz_object_id")
    val name = varchar("name")
    val api = varchar("api")
    val type = varchar("type")
    val active = boolean("active")
    val history = boolean("history")
    val defaultValue = varchar("default_value")
    val formula = varchar("formula")
    val maxLength = int("max_length")
    val minLength = int("min_length")
    val visibleRow = int("visible_row")
    val dictId = varchar("dict_id")
    val masterFieldId = varchar("master_field_id")
    val optionalNumber = int("optional_number")
    val refId = varchar("ref_id")
    val refListLabel = varchar("ref_list_label")
    val refDeletePolicy = varchar("ref_delete_policy")
    val queryFilter = varchar("query_filter")
    val sId = varchar("s_id")
    val sFieldId = varchar("s_field_id")
    val sType = varchar("s_type")
    val genRule = varchar("gen_rule")
    val remark = varchar("remark")
    val help = varchar("help")
    val tmp = varchar("tmp")
    val joinViewName = varchar("join_view_name")
    val standardType = varchar("standard_type")
    val isPredefined = boolean("is_predefined")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectField(
        sourceColumn = requireNotNull(row[this.sourceColumn]) { "field BizObjectField.sourceColumn should not be null" },
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field BizObjectField.bizObjectId should not be null" },
        name = requireNotNull(row[this.name]) { "field BizObjectField.name should not be null" },
        api = requireNotNull(row[this.api]) { "field BizObjectField.api should not be null" },
        type = requireNotNull(row[this.type]) { "field BizObjectField.type should not be null" },
        active = row[this.active],
        history = row[this.history],
        defaultValue = row[this.defaultValue],
        formula = row[this.formula],
        maxLength = row[this.maxLength],
        minLength = row[this.minLength],
        visibleRow = row[this.visibleRow],
        dictId = row[this.dictId],
        masterFieldId = row[this.masterFieldId],
        optionalNumber = row[this.optionalNumber],
        refId = row[this.refId],
        refListLabel = row[this.refListLabel],
        refDeletePolicy = row[this.refDeletePolicy],
        queryFilter = row[this.queryFilter],
        sId = row[this.sId],
        sFieldId = row[this.sFieldId],
        sType = row[this.sType],
        genRule = row[this.genRule],
        remark = row[this.remark],
        help = row[this.help],
        tmp = row[this.tmp],
        joinViewName = row[this.joinViewName],
        standardType = row[this.standardType],
        isPredefined = requireNotNull(row[this.isPredefined]) { "field BizObjectField.isPredefined should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectField.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field BizObjectField.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field BizObjectField.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field BizObjectField.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field BizObjectField.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field BizObjectField.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field BizObjectField.modifyDate should not be null" }
    }
}
