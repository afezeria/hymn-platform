package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.BizObject
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjects(alias: String? = null) :
    BaseTable<BizObject>("core_biz_object", schema = "hymn", alias = alias) {

    val name = varchar("name")
    val api = varchar("api")
    val sourceTable = varchar("source_table")
    val active = boolean("active")
    val type = varchar("type")
    val remoteUrl = varchar("remote_url")
    val remoteToken = varchar("remote_token")
    val moduleApi = varchar("module_api")
    val remark = varchar("remark")
    val canInsert = boolean("can_insert")
    val canUpdate = boolean("can_update")
    val canDelete = boolean("can_delete")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObject(
        name = requireNotNull(row[this.name]) { "field BizObject.name should not be null" },
        api = requireNotNull(row[this.api]) { "field BizObject.api should not be null" },
        sourceTable = row[this.sourceTable],
        active = requireNotNull(row[this.active]) { "field BizObject.active should not be null" },
        type = requireNotNull(row[this.type]) { "field BizObject.type should not be null" },
        remoteUrl = row[this.remoteUrl],
        remoteToken = row[this.remoteToken],
        moduleApi = row[this.moduleApi],
        remark = requireNotNull(row[this.remark]) { "field BizObject.remark should not be null" },
        canInsert = row[this.canInsert],
        canUpdate = row[this.canUpdate],
        canDelete = row[this.canDelete],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObject.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field BizObject.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field BizObject.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field BizObject.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field BizObject.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field BizObject.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field BizObject.modifyDate should not be null" }
    }
}
