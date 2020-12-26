package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.Dict
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreDicts(alias: String? = null) :
    BaseTable<Dict>("core_dict", schema = "hymn", alias = alias) {

    val fieldId = varchar("field_id")
    val parentDictId = varchar("parent_dict_id")
    val name = varchar("name")
    val api = varchar("api")
    val remark = varchar("remark")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Dict(
        fieldId = row[this.fieldId],
        parentDictId = row[this.parentDictId],
        name = requireNotNull(row[this.name]) { "field Dict.name should not be null" },
        api = requireNotNull(row[this.api]) { "field Dict.api should not be null" },
        remark = row[this.remark],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field Dict.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field Dict.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field Dict.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field Dict.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field Dict.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field Dict.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field Dict.modifyDate should not be null" }
    }
}
