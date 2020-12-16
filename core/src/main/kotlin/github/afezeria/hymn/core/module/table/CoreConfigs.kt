package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.Config

/**
 * @author afezeria
 */
class CoreConfigs(alias: String? = null) :
    BaseTable<Config>("core_config", schema = "hymn", alias = alias) {

    val key = varchar("key")
    val value = varchar("value")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Config(
        key = requireNotNull(row[this.key]) { "field Config.key should not be null" },
        value = requireNotNull(row[this.value]) { "field Config.value should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field Config.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field Config.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field Config.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field Config.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field Config.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field Config.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field Config.modifyDate should not be null" }
    }
}
