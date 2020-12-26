package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.CustomButton
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCustomButtons(alias: String? = null) :
    BaseTable<CustomButton>("core_custom_button", schema = "hymn", alias = alias) {

    val remark = varchar("remark")
    val bizObjectId = varchar("biz_object_id")
    val name = varchar("name")
    val api = varchar("api")
    val clientType = varchar("client_type")
    val action = varchar("action")
    val content = varchar("content")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = CustomButton(
        remark = row[this.remark],
        bizObjectId = row[this.bizObjectId],
        name = requireNotNull(row[this.name]) { "field CustomButton.name should not be null" },
        api = requireNotNull(row[this.api]) { "field CustomButton.api should not be null" },
        clientType = requireNotNull(row[this.clientType]) { "field CustomButton.clientType should not be null" },
        action = requireNotNull(row[this.action]) { "field CustomButton.action should not be null" },
        content = requireNotNull(row[this.content]) { "field CustomButton.content should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field CustomButton.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field CustomButton.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field CustomButton.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field CustomButton.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field CustomButton.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field CustomButton.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field CustomButton.modifyDate should not be null" }
    }
}
