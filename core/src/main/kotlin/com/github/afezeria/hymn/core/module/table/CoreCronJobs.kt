package com.github.afezeria.hymn.core.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.core.module.entity.CronJob
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCronJobs(alias: String? = null) :
    AbstractTable<CronJob>("core_cron_job", schema = "hymn", alias = alias) {

    val active = boolean("active")
    val sharedCodeId = varchar("shared_code_id")
    val cron = varchar("cron")
    val startDateTime = datetime("start_date_time")
    val endDateTime = datetime("end_date_time")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
