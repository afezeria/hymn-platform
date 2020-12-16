package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm

/**
 * @author afezeria
 */
class CoreBizObjectFieldPerms(alias: String? = null) :
    BaseTable<BizObjectFieldPerm>("core_biz_object_field_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val bizObjectId = varchar("biz_object_id")
    val fieldId = varchar("field_id")
    val pRead = boolean("p_read")
    val pEdit = boolean("p_edit")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectFieldPerm(
        roleId = requireNotNull(row[this.roleId]) { "field BizObjectFieldPerm.roleId should not be null" },
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field BizObjectFieldPerm.bizObjectId should not be null" },
        fieldId = requireNotNull(row[this.fieldId]) { "field BizObjectFieldPerm.fieldId should not be null" },
        pRead = requireNotNull(row[this.pRead]) { "field BizObjectFieldPerm.pRead should not be null" },
        pEdit = requireNotNull(row[this.pEdit]) { "field BizObjectFieldPerm.pEdit should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectFieldPerm.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field BizObjectFieldPerm.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field BizObjectFieldPerm.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field BizObjectFieldPerm.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field BizObjectFieldPerm.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field BizObjectFieldPerm.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field BizObjectFieldPerm.modifyDate should not be null" }
    }
}
