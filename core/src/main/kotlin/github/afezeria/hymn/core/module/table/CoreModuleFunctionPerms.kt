package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreModuleFunctionPerms(alias: String? = null) :
    AbstractTable<ModuleFunctionPerm>("core_module_function_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val moduleApi = varchar("module_api")
    val functionApi = varchar("function_api")
    val perm = boolean("perm")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
