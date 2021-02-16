package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.common.db.AbstractTable
import github.afezeria.hymn.core.module.entity.BizObjectTrigger
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreBizObjectTriggers(alias: String? = null) :
    AbstractTable<BizObjectTrigger>("core_biz_object_trigger", schema = "hymn", alias = alias) {

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
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
