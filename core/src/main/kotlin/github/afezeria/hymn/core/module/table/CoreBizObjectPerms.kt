package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.BizObjectPerm
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectPerms(alias: String? = null) :
    AbstractTable<BizObjectPerm>("core_biz_object_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val bizObjectId = varchar("biz_object_id")
    val ins = boolean("ins")
    val upd = boolean("upd")
    val del = boolean("del")
    val que = boolean("que")
    val queryWithAccountTree = boolean("query_with_account_tree")
    val queryWithOrg = boolean("query_with_org")
    val queryWithOrgTree = boolean("query_with_org_tree")
    val queryAll = boolean("query_all")
    val editAll = boolean("edit_all")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
