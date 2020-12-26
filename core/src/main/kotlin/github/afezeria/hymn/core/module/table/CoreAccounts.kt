package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.Account
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreAccounts(alias: String? = null) :
    BaseTable<Account>("core_account", schema = "hymn", alias = alias) {

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
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Account(
        lockTime = requireNotNull(row[this.lockTime]) { "field Account.lockTime should not be null" },
        name = requireNotNull(row[this.name]) { "field Account.name should not be null" },
        username = requireNotNull(row[this.username]) { "field Account.username should not be null" },
        password = requireNotNull(row[this.password]) { "field Account.password should not be null" },
        onlineRule = requireNotNull(row[this.onlineRule]) { "field Account.onlineRule should not be null" },
        active = requireNotNull(row[this.active]) { "field Account.active should not be null" },
        admin = requireNotNull(row[this.admin]) { "field Account.admin should not be null" },
        root = requireNotNull(row[this.root]) { "field Account.root should not be null" },
        leaderId = row[this.leaderId],
        orgId = requireNotNull(row[this.orgId]) { "field Account.orgId should not be null" },
        roleId = requireNotNull(row[this.roleId]) { "field Account.roleId should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field Account.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field Account.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field Account.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field Account.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field Account.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field Account.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field Account.modifyDate should not be null" }
    }
}
