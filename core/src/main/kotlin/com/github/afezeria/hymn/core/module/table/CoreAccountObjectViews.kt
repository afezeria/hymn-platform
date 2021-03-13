package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.AccountObjectView
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreAccountObjectViews(alias: String? = null) :
    AbstractTable<AccountObjectView>("core_account_object_view", schema = "hymn", alias = alias) {

    val copyId = varchar("copy_id")
    val remark = varchar("remark")
    val globalView = boolean("global_view")
    val defaultView = boolean("default_view")
    val accountId = varchar("account_id")
    val bizObjectId = varchar("biz_object_id")
    val name = varchar("name")
    val viewJson = varchar("view_json")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
