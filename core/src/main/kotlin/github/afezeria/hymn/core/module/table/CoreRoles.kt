package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.Role
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreRoles(alias: String? = null) :
    BaseTable<Role>("core_role", schema = "hymn", alias = alias) {

    val name = varchar("name")
    val remark = varchar("remark")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Role(
        name = requireNotNull(row[this.name]) { "field Role.name should not be null" },
        remark = row[this.remark],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field Role.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field Role.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field Role.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field Role.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field Role.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field Role.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field Role.modifyDate should not be null" }
    }
}
