package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.CustomPage
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCustomPages(alias: String? = null) :
    BaseTable<CustomPage>("core_custom_page", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val name = varchar("name")
    val remark = varchar("remark")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = CustomPage(
        api = requireNotNull(row[this.api]) { "field CustomPage.api should not be null" },
        name = requireNotNull(row[this.name]) { "field CustomPage.name should not be null" },
        remark = row[this.remark],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field CustomPage.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field CustomPage.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field CustomPage.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field CustomPage.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field CustomPage.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field CustomPage.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field CustomPage.modifyDate should not be null" }
    }
}
