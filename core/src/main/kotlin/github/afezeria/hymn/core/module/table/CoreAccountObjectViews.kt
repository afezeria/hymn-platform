package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.AccountObjectView
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreAccountObjectViews(alias: String? = null) :
    BaseTable<AccountObjectView>("core_account_object_view", schema = "hymn", alias = alias) {

    val copyId = varchar("copy_id")
    val remark = varchar("remark")
    val globalView = boolean("global_view")
    val defaultView = boolean("default_view")
    val accountId = varchar("account_id")
    val bizObjectId = varchar("biz_object_id")
    val name = varchar("name")
    val viewJson = varchar("view_json")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = AccountObjectView(
        copyId = row[this.copyId],
        remark = row[this.remark],
        globalView = requireNotNull(row[this.globalView]) { "field AccountObjectView.globalView should not be null" },
        defaultView = requireNotNull(row[this.defaultView]) { "field AccountObjectView.defaultView should not be null" },
        accountId = requireNotNull(row[this.accountId]) { "field AccountObjectView.accountId should not be null" },
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field AccountObjectView.bizObjectId should not be null" },
        name = requireNotNull(row[this.name]) { "field AccountObjectView.name should not be null" },
        viewJson = requireNotNull(row[this.viewJson]) { "field AccountObjectView.viewJson should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field AccountObjectView.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field AccountObjectView.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field AccountObjectView.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field AccountObjectView.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field AccountObjectView.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field AccountObjectView.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field AccountObjectView.modifyDate should not be null" }
    }
}
