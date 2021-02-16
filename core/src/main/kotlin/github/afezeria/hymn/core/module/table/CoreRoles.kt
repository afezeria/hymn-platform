package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.Role
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreRoles(alias: String? = null) :
    AbstractTable<Role>("core_role", schema = "hymn", alias = alias) {

    val name = varchar("name")
    val remark = varchar("remark")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
