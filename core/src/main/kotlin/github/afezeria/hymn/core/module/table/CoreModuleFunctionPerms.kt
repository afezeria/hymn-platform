package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm

/**
 * @author afezeria
 */
class CoreModuleFunctionPerms(alias: String? = null) :
    BaseTable<ModuleFunctionPerm>("core_module_function_perm", schema = "hymn", alias = alias) {

    val roleId = varchar("role_id")
    val moduleFunctionId = varchar("module_function_id")
    val perm = boolean("perm")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = ModuleFunctionPerm(
        roleId = requireNotNull(row[this.roleId]) { "field ModuleFunctionPerm.roleId should not be null" },
        moduleFunctionId = requireNotNull(row[this.moduleFunctionId]) { "field ModuleFunctionPerm.moduleFunctionId should not be null" },
        perm = row[this.perm],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field ModuleFunctionPerm.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field ModuleFunctionPerm.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field ModuleFunctionPerm.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field ModuleFunctionPerm.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field ModuleFunctionPerm.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field ModuleFunctionPerm.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field ModuleFunctionPerm.modifyDate should not be null" }
    }
}
