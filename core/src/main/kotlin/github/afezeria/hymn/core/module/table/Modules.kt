package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.Module
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class Modules(alias: String? = null) :
    BaseTable<Module>("core_module", schema = "hymn", alias = alias) {

    val api = varchar("api")
    val name = varchar("name")
    val remark = varchar("remark")
    val version = varchar("version")
    val createDate = datetime("create_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) =
        Module(
            api = requireNotNull(row[this.api]) { "field Module.api should not be null" },
            name = requireNotNull(row[this.name]) { "field Module.name should not be null" },
            remark = requireNotNull(row[this.remark]) { "field Module.remark should not be null" },
            version = requireNotNull(row[this.version]) { "field Module.version should not be null" },
            create_date = requireNotNull(row[this.createDate]) { "field Module.createDate should not be null" },
        )
}