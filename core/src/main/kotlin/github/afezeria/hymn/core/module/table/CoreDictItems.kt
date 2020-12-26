package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.DictItem
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreDictItems(alias: String? = null) :
    BaseTable<DictItem>("core_dict_item", schema = "hymn", alias = alias) {

    val dictId = varchar("dict_id")
    val name = varchar("name")
    val code = varchar("code")
    val parentCode = varchar("parent_code")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = DictItem(
        dictId = requireNotNull(row[this.dictId]) { "field DictItem.dictId should not be null" },
        name = requireNotNull(row[this.name]) { "field DictItem.name should not be null" },
        code = requireNotNull(row[this.code]) { "field DictItem.code should not be null" },
        parentCode = row[this.parentCode],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field DictItem.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field DictItem.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field DictItem.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field DictItem.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field DictItem.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field DictItem.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field DictItem.modifyDate should not be null" }
    }
}
