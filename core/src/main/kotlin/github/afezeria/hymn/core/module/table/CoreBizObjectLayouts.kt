package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.BizObjectLayout

/**
 * @author afezeria
 */
class CoreBizObjectLayouts(alias: String? = null) :
    BaseTable<BizObjectLayout>("core_biz_object_layout", schema = "hymn", alias = alias) {

    val bizObjectId = varchar("biz_object_id")
    val name = varchar("name")
    val remark = varchar("remark")
    val relFieldJsonArr = varchar("rel_field_json_arr")
    val pcReadLayoutJson = varchar("pc_read_layout_json")
    val pcEditLayoutJson = varchar("pc_edit_layout_json")
    val mobileReadLayoutJson = varchar("mobile_read_layout_json")
    val mobileEditLayoutJson = varchar("mobile_edit_layout_json")
    val previewLayoutJson = varchar("preview_layout_json")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectLayout(
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field BizObjectLayout.bizObjectId should not be null" },
        name = requireNotNull(row[this.name]) { "field BizObjectLayout.name should not be null" },
        remark = row[this.remark],
        relFieldJsonArr = requireNotNull(row[this.relFieldJsonArr]) { "field BizObjectLayout.relFieldJsonArr should not be null" },
        pcReadLayoutJson = requireNotNull(row[this.pcReadLayoutJson]) { "field BizObjectLayout.pcReadLayoutJson should not be null" },
        pcEditLayoutJson = requireNotNull(row[this.pcEditLayoutJson]) { "field BizObjectLayout.pcEditLayoutJson should not be null" },
        mobileReadLayoutJson = requireNotNull(row[this.mobileReadLayoutJson]) { "field BizObjectLayout.mobileReadLayoutJson should not be null" },
        mobileEditLayoutJson = requireNotNull(row[this.mobileEditLayoutJson]) { "field BizObjectLayout.mobileEditLayoutJson should not be null" },
        previewLayoutJson = requireNotNull(row[this.previewLayoutJson]) { "field BizObjectLayout.previewLayoutJson should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectLayout.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field BizObjectLayout.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field BizObjectLayout.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field BizObjectLayout.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field BizObjectLayout.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field BizObjectLayout.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field BizObjectLayout.modifyDate should not be null" }
    }
}
