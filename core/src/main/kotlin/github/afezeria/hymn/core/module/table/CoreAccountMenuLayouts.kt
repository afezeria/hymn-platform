package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.AccountMenuLayout
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreAccountMenuLayouts(alias: String? = null) :
    AbstractTable<AccountMenuLayout>("core_account_menu_layout", schema = "hymn", alias = alias) {

    val accountId = varchar("account_id")
    val clientType = varchar("client_type").primaryKey()
    val layoutJson = varchar("layout_json")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
