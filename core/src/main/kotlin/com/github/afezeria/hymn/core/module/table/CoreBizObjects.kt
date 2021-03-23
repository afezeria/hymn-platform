package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.BizObject
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjects(alias: String? = null) :
    AbstractTable<BizObject>("core_biz_object", schema = "hymn", alias = alias) {

    val name = varchar("name")
    val api = varchar("api")
    val sourceTable = varchar("source_table")
    val active = boolean("active")
    val type = varchar("type")
    val functionId = varchar("function_id")
    val remoteUrl = varchar("remote_url")
    val remoteToken = varchar("remote_token")
    val moduleApi = varchar("module_api")
    val remark = varchar("remark")
    val canInsert = boolean("can_insert")
    val canUpdate = boolean("can_update")
    val canDelete = boolean("can_delete")
    val canSoftDelete = boolean("can_soft_delete")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
