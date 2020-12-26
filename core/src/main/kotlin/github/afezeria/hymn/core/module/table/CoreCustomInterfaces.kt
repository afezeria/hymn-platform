package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.CustomInterface
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCustomInterfaces(alias: String? = null) :
    BaseTable<CustomInterface>("core_custom_interface", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val name = varchar("name")
    val code = varchar("code")
    val active = boolean("active")
    val lang = varchar("lang")
    val optionText = varchar("option_text")
    val remark = varchar("remark")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = CustomInterface(
        api = requireNotNull(row[this.api]) { "field CustomInterface.api should not be null" },
        name = requireNotNull(row[this.name]) { "field CustomInterface.name should not be null" },
        code = requireNotNull(row[this.code]) { "field CustomInterface.code should not be null" },
        active = requireNotNull(row[this.active]) { "field CustomInterface.active should not be null" },
        lang = requireNotNull(row[this.lang]) { "field CustomInterface.lang should not be null" },
        optionText = row[this.optionText],
        remark = row[this.remark],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field CustomInterface.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field CustomInterface.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field CustomInterface.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field CustomInterface.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field CustomInterface.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field CustomInterface.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field CustomInterface.modifyDate should not be null" }
    }
}
