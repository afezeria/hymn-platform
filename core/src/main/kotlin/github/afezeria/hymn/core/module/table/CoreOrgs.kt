package github.afezeria.hymn.core.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.core.module.entity.Org

/**
 * @author afezeria
 */
class CoreOrgs(alias: String? = null) :
    BaseTable<Org>("core_org", schema = "hymn", alias = alias) {

    val name = varchar("name")
    val directorId = varchar("director_id")
    val deputyDirectorId = varchar("deputy_director_id")
    val parentId = varchar("parent_id")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Org(
        name = requireNotNull(row[this.name]) { "field Org.name should not be null" },
        directorId = row[this.directorId],
        deputyDirectorId = row[this.deputyDirectorId],
        parentId = row[this.parentId],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field Org.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field Org.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field Org.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field Org.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field Org.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field Org.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field Org.modifyDate should not be null" }
    }
}
