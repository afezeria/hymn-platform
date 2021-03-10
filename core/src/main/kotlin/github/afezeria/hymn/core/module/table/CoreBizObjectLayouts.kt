package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.BizObjectLayout
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectLayouts(alias: String? = null) :
    AbstractTable<BizObjectLayout>("core_biz_object_layout", schema = "hymn", alias = alias) {

    val bizObjectId = varchar("biz_object_id")
    val name = varchar("name")
    val remark = varchar("remark")
    val componentJson = varchar("component_json")
    val pcReadLayoutJson = varchar("pc_read_layout_json")
    val pcEditLayoutJson = varchar("pc_edit_layout_json")
    val mobileReadLayoutJson = varchar("mobile_read_layout_json")
    val mobileEditLayoutJson = varchar("mobile_edit_layout_json")
    val previewLayoutJson = varchar("preview_layout_json")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
