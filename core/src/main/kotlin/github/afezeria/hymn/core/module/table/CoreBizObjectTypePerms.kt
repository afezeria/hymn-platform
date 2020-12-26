package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectTypePerms(alias: String? = null) :
    BaseTable<BizObjectTypePerm>("core_biz_object_type_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val bizObjectId = varchar("biz_object_id")
    val typeId = varchar("type_id")
    val visible = boolean("visible")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectTypePerm(
        roleId = requireNotNull(row[this.roleId]) { "field BizObjectTypePerm.roleId should not be null" },
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field BizObjectTypePerm.bizObjectId should not be null" },
        typeId = requireNotNull(row[this.typeId]) { "field BizObjectTypePerm.typeId should not be null" },
        visible = requireNotNull(row[this.visible]) { "field BizObjectTypePerm.visible should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectTypePerm.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field BizObjectTypePerm.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field BizObjectTypePerm.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field BizObjectTypePerm.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field BizObjectTypePerm.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field BizObjectTypePerm.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field BizObjectTypePerm.modifyDate should not be null" }
    }
}
