package github.afezeria.hymn.core.module.table

import github.afezeria.hymn.core.module.entity.CronJob
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class CoreCronJobs(alias: String? = null) :
    BaseTable<CronJob>("core_cron_job", schema = "hymn", alias = alias) {

    val active = boolean("active")
    val sharedCodeId = varchar("shared_code_id")
    val cron = varchar("cron")
    val startDateTime = datetime("start_date_time")
    val endDateTime = datetime("end_date_time")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = CronJob(
        active = requireNotNull(row[this.active]) { "field CronJob.active should not be null" },
        sharedCodeId = requireNotNull(row[this.sharedCodeId]) { "field CronJob.sharedCodeId should not be null" },
        cron = requireNotNull(row[this.cron]) { "field CronJob.cron should not be null" },
        startDateTime = requireNotNull(row[this.startDateTime]) { "field CronJob.startDateTime should not be null" },
        endDateTime = requireNotNull(row[this.endDateTime]) { "field CronJob.endDateTime should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field CronJob.id should not be null" }
        it.createById =
            requireNotNull(row[this.createById]) { "field CronJob.createById should not be null" }
        it.createBy =
            requireNotNull(row[this.createBy]) { "field CronJob.createBy should not be null" }
        it.modifyById =
            requireNotNull(row[this.modifyById]) { "field CronJob.modifyById should not be null" }
        it.modifyBy =
            requireNotNull(row[this.modifyBy]) { "field CronJob.modifyBy should not be null" }
        it.createDate =
            requireNotNull(row[this.createDate]) { "field CronJob.createDate should not be null" }
        it.modifyDate =
            requireNotNull(row[this.modifyDate]) { "field CronJob.modifyDate should not be null" }
    }
}
