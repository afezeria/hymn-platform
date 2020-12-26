package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.AccountMenuLayout
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreAccountMenuLayouts(alias: String? = null) :
    BaseTable<AccountMenuLayout>("core_account_menu_layout", schema = "hymn", alias = alias) {

    val accountId = varchar("account_id")
    val clientType = varchar("client_type")
    val layoutJson = varchar("layout_json")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = AccountMenuLayout(
        accountId = requireNotNull(row[this.accountId]) { "field AccountMenuLayout.accountId should not be null" },
        clientType = requireNotNull(row[this.clientType]) { "field AccountMenuLayout.clientType should not be null" },
        layoutJson = requireNotNull(row[this.layoutJson]) { "field AccountMenuLayout.layoutJson should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field AccountMenuLayout.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field AccountMenuLayout.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field AccountMenuLayout.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field AccountMenuLayout.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field AccountMenuLayout.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field AccountMenuLayout.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field AccountMenuLayout.modifyDate should not be null" }
    }
}
