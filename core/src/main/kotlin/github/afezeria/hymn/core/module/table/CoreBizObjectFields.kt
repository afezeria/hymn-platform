package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.BizObjectField
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectFields(alias: String? = null) :
    AbstractTable<BizObjectField>("core_biz_object_field", schema = "hymn", alias = alias) {

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
    val filterList = varchar("filter_list")
    val sId = varchar("s_id")
    val sFieldId = varchar("s_field_id")
    val sType = varchar("s_type")
    val genRule = varchar("gen_rule")
    val remark = varchar("remark")
    val help = varchar("help")
    val joinViewName = varchar("join_view_name")
    val standardType = varchar("standard_type")
    val predefined = boolean("predefined")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
