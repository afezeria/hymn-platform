package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.ButtonPerm
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreButtonPerms(alias: String? = null) :
    BaseTable<ButtonPerm>("core_button_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val buttonId = varchar("button_id")
    val visible = boolean("visible")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = ButtonPerm(
        roleId = requireNotNull(row[this.roleId]) { "field ButtonPerm.roleId should not be null" },
        buttonId = requireNotNull(row[this.buttonId]) { "field ButtonPerm.buttonId should not be null" },
        visible = requireNotNull(row[this.visible]) { "field ButtonPerm.visible should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field ButtonPerm.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field ButtonPerm.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field ButtonPerm.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field ButtonPerm.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field ButtonPerm.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field ButtonPerm.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field ButtonPerm.modifyDate should not be null" }
    }
}
