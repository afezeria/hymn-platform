package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout

/**
 * @author afezeria
 */
class CoreBizObjectTypeLayouts(alias: String? = null) :
    BaseTable<BizObjectTypeLayout>("core_biz_object_type_layout", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val bizObjectId = varchar("biz_object_id")
    val typeId = varchar("type_id")
    val layoutId = varchar("layout_id")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectTypeLayout(
        roleId = requireNotNull(row[this.roleId]) { "field BizObjectTypeLayout.roleId should not be null" },
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field BizObjectTypeLayout.bizObjectId should not be null" },
        typeId = requireNotNull(row[this.typeId]) { "field BizObjectTypeLayout.typeId should not be null" },
        layoutId = requireNotNull(row[this.layoutId]) { "field BizObjectTypeLayout.layoutId should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectTypeLayout.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field BizObjectTypeLayout.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field BizObjectTypeLayout.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field BizObjectTypeLayout.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field BizObjectTypeLayout.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field BizObjectTypeLayout.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field BizObjectTypeLayout.modifyDate should not be null" }
    }
}
