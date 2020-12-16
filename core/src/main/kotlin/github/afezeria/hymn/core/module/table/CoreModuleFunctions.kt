package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.ModuleFunction

/**
 * @author afezeria
 */
class CoreModuleFunctions(alias: String? = null) :
    BaseTable<ModuleFunction>("core_module_function", schema = "hymn", alias = alias) {

    val moduleId = varchar("module_id")
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

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = ModuleFunction(
        moduleId = requireNotNull(row[this.moduleId]) { "field ModuleFunction.moduleId should not be null" },
        api = requireNotNull(row[this.api]) { "field ModuleFunction.api should not be null" },
        name = requireNotNull(row[this.name]) { "field ModuleFunction.name should not be null" },
        remark = row[this.remark],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field ModuleFunction.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field ModuleFunction.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field ModuleFunction.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field ModuleFunction.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field ModuleFunction.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field ModuleFunction.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field ModuleFunction.modifyDate should not be null" }
    }
}
