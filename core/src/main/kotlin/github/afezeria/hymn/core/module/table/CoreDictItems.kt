package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.DictItem
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreDictItems(alias: String? = null) :
    AbstractTable<DictItem>("core_dict_item", schema = "hymn", alias = alias) {

    val dictId = varchar("dict_id")
    val name = varchar("name")
    val code = varchar("code")
    val parentCode = varchar("parent_code")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
