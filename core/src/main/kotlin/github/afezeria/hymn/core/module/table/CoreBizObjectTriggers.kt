package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.BizObjectTrigger
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*

/**
 * @author afezeria
 */
class CoreBizObjectTriggers(alias: String? = null) :
    BaseTable<BizObjectTrigger>("core_biz_object_trigger", schema = "hymn", alias = alias) {

    val active = boolean("active")
    val remark = varchar("remark")
    val bizObjectId = varchar("biz_object_id")
    val name = varchar("name")
    val api = varchar("api")
    val lang = varchar("lang")
    val optionText = varchar("option_text")
    val ord = int("ord")
    val event = varchar("event")
    val code = varchar("code")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = BizObjectTrigger(
        active = requireNotNull(row[this.active]) { "field BizObjectTrigger.active should not be null" },
        remark = row[this.remark],
        bizObjectId = requireNotNull(row[this.bizObjectId]) { "field BizObjectTrigger.bizObjectId should not be null" },
        name = requireNotNull(row[this.name]) { "field BizObjectTrigger.name should not be null" },
        api = requireNotNull(row[this.api]) { "field BizObjectTrigger.api should not be null" },
        lang = requireNotNull(row[this.lang]) { "field BizObjectTrigger.lang should not be null" },
        optionText = row[this.optionText],
        ord = requireNotNull(row[this.ord]) { "field BizObjectTrigger.ord should not be null" },
        event = requireNotNull(row[this.event]) { "field BizObjectTrigger.event should not be null" },
        code = requireNotNull(row[this.code]) { "field BizObjectTrigger.code should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field BizObjectTrigger.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field BizObjectTrigger.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field BizObjectTrigger.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field BizObjectTrigger.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field BizObjectTrigger.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field BizObjectTrigger.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field BizObjectTrigger.modifyDate should not be null" }
    }
}
