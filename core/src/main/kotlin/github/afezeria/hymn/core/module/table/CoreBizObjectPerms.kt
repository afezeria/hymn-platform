package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.BizObjectPerm
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectPerms(alias: String? = null) :
    BaseTable<BizObjectPerm>("core_biz_object_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val bizObjectId = varchar("biz_object_id")
    val ins = boolean("ins")
    val upd = boolean("upd")
    val del = boolean("del")
    val que = boolean("que")
    val queryWithAccountTree = boolean("query_with_account_tree")
    val queryWithDept = boolean("query_with_dept")
    val queryWithDeptTree = boolean("query_with_dept_tree")
    val queryAll = boolean("query_all")
    val editAll = boolean("edit_all")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectPerm(
        roleId = requireNotNull(row[this.roleId]) { "field BizObjectPerm.roleId should not be null" },
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field BizObjectPerm.bizObjectId should not be null" },
        ins = requireNotNull(row[this.ins]) { "field BizObjectPerm.ins should not be null" },
        upd = requireNotNull(row[this.upd]) { "field BizObjectPerm.upd should not be null" },
        del = requireNotNull(row[this.del]) { "field BizObjectPerm.del should not be null" },
        que = requireNotNull(row[this.que]) { "field BizObjectPerm.que should not be null" },
        queryWithAccountTree = requireNotNull(row[this.queryWithAccountTree]) { "field BizObjectPerm.queryWithAccountTree should not be null" },
        queryWithDept = requireNotNull(row[this.queryWithDept]) { "field BizObjectPerm.queryWithDept should not be null" },
        queryWithDeptTree = requireNotNull(row[this.queryWithDeptTree]) { "field BizObjectPerm.queryWithDeptTree should not be null" },
        queryAll = requireNotNull(row[this.queryAll]) { "field BizObjectPerm.queryAll should not be null" },
        editAll = requireNotNull(row[this.editAll]) { "field BizObjectPerm.editAll should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectPerm.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field BizObjectPerm.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field BizObjectPerm.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field BizObjectPerm.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field BizObjectPerm.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field BizObjectPerm.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field BizObjectPerm.modifyDate should not be null" }
    }
}
