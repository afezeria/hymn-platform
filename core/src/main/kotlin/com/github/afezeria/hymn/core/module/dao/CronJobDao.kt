package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.CronJob
import com.github.afezeria.hymn.core.module.table.CoreCronJobs
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