package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.CronJob
import github.afezeria.hymn.core.module.table.CoreCronJobs
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CronJobDao(
    databaseService: DatabaseService
) : AbstractDao<CronJob, CoreCronJobs>(
    table = CoreCronJobs(),
    databaseService = databaseService
) {


    fun selectBySharedCodeId(
        sharedCodeId: String,
    ): MutableList<CronJob> {
        return select({ it.sharedCodeId eq sharedCodeId })
    }
}