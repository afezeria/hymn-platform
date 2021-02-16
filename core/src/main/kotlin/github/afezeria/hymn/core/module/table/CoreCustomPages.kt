package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.CustomPage
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCustomPages(alias: String? = null) :
    AbstractTable<CustomPage>("core_custom_page", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val name = varchar("name")
    val md5 = varchar("md5")
    val remark = varchar("remark")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
