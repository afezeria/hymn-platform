package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.Account
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreAccounts(alias: String? = null) :
    AbstractTable<Account>("core_account", schema = "hymn", alias = alias) {

    val lockTime = datetime("lock_time")
    val name = varchar("name")
    val username = varchar("username")
    val password = varchar("password")
    val onlineRule = varchar("online_rule")
    val active = boolean("active")
    val admin = boolean("admin")
    val root = boolean("root")
    val leaderId = varchar("leader_id")
    val orgId = varchar("org_id")
    val roleId = varchar("role_id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
