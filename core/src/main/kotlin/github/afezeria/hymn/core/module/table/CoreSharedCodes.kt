package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.SharedCode

/**
 * @author afezeria
 */
class CoreSharedCodes(alias: String? = null) :
    BaseTable<SharedCode>("core_shared_code", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val type = varchar("type")
    val code = varchar("code")
    val lang = varchar("lang")
    val optionText = varchar("option_text")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = SharedCode(
        api = requireNotNull(row[this.api]) { "field SharedCode.api should not be null" },
        type = requireNotNull(row[this.type]) { "field SharedCode.type should not be null" },
        code = requireNotNull(row[this.code]) { "field SharedCode.code should not be null" },
        lang = requireNotNull(row[this.lang]) { "field SharedCode.lang should not be null" },
        optionText = row[this.optionText],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field SharedCode.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field SharedCode.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field SharedCode.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field SharedCode.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field SharedCode.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field SharedCode.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field SharedCode.modifyDate should not be null" }
    }
}
