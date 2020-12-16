package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.BusinessCodeRef

/**
 * @author afezeria
 */
class CoreBusinessCodeRefs(alias: String? = null) :
    BaseTable<BusinessCodeRef>("core_business_code_ref", schema = "hymn", alias = alias) {

    val triggerId = varchar("trigger_id")
    val interfaceId = varchar("interface_id")
    val sharedCodeId = varchar("shared_code_id")
    val bizObjectId = varchar("biz_object_id")
    val fieldId = varchar("field_id")
    val orgId = varchar("org_id")
    val roleId = varchar("role_id")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BusinessCodeRef(
        triggerId = row[this.triggerId],
        interfaceId = row[this.interfaceId],
        sharedCodeId = row[this.sharedCodeId],
        bizObjectId = row[this.bizObjectId],
        fieldId = row[this.fieldId],
        orgId = row[this.orgId],
        roleId = row[this.roleId],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BusinessCodeRef.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field BusinessCodeRef.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field BusinessCodeRef.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field BusinessCodeRef.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field BusinessCodeRef.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field BusinessCodeRef.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field BusinessCodeRef.modifyDate should not be null" }
    }
}
