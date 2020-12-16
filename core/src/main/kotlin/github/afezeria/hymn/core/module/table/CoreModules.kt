package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.Module

/**
 * @author afezeria
 */
class CoreModules(alias: String? = null) :
    BaseTable<Module>("core_module", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val name = varchar("name")
    val remark = varchar("remark")
    val version = varchar("version")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Module(
        api = row[this.api],
        name = requireNotNull(row[this.name]) { "field Module.name should not be null" },
        remark = requireNotNull(row[this.remark]) { "field Module.remark should not be null" },
        version = requireNotNull(row[this.version]) { "field Module.version should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field Module.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field Module.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field Module.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field Module.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field Module.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field Module.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field Module.modifyDate should not be null" }
    }
}
