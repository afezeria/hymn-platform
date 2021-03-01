package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.BizObjectType
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectTypes(alias: String? = null) :
    AbstractTable<BizObjectType>("core_biz_object_type", schema = "hymn", alias = alias) {

    val bizObjectId = varchar("biz_object_id")
    val name = varchar("name")
    val remark = varchar("remark")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
