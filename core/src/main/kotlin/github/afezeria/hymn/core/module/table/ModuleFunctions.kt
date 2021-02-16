package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.ModuleFunction
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class ModuleFunctions(alias: String? = null) :
    BaseTable<ModuleFunction>("core_module_function", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val name = varchar("name")
    val moduleApi = varchar("module_api")
    val remark = varchar("remark")
    val createDate = datetime("create_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) =
        ModuleFunction(
            api = requireNotNull(row[this.api]) { "field ModuleFunction.api should not be null" },
            name = requireNotNull(row[this.name]) { "field ModuleFunction.name should not be null" },
            moduleApi = requireNotNull(row[this.name]) { "field ModuleFunction.moduleApi should not be null" },
            remark = requireNotNull(row[this.remark]) { "field ModuleFunction.remark should not be null" },
            create_date = requireNotNull(row[this.createDate]) { "field ModuleFunction.createDate should not be null" },
        )
}