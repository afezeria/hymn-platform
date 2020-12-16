package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.CustomComponent

/**
 * @author afezeria
 */
class CoreCustomComponents(alias: String? = null) :
    BaseTable<CustomComponent>("core_custom_component", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val name = varchar("name")
    val code = varchar("code")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = CustomComponent(
        api = requireNotNull(row[this.api]) { "field CustomComponent.api should not be null" },
        name = requireNotNull(row[this.name]) { "field CustomComponent.name should not be null" },
        code = requireNotNull(row[this.code]) { "field CustomComponent.code should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field CustomComponent.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field CustomComponent.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field CustomComponent.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field CustomComponent.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field CustomComponent.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field CustomComponent.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field CustomComponent.modifyDate should not be null" }
    }
}
