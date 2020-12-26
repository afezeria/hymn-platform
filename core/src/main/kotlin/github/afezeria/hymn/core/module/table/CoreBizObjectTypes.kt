package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.BizObjectType
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectTypes(alias: String? = null) :
    BaseTable<BizObjectType>("core_biz_object_type", schema = "hymn", alias = alias) {

    val bizObjectId = varchar("biz_object_id")
    val name = varchar("name")
    val active = boolean("active")
    val remark = varchar("remark")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectType(
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field BizObjectType.bizObjectId should not be null" },
        name = requireNotNull(row[this.name]) { "field BizObjectType.name should not be null" },
        active = requireNotNull(row[this.active]) { "field BizObjectType.active should not be null" },
        remark = row[this.remark],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectType.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field BizObjectType.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field BizObjectType.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field BizObjectType.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field BizObjectType.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field BizObjectType.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field BizObjectType.modifyDate should not be null" }
    }
}
