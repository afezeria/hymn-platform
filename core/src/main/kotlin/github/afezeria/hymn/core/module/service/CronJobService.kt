package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.CronJobDto
import github.afezeria.hymn.core.module.entity.CronJob

/**
 * @author afezeria
 */
internal interface CronJobService {

    fun removeById(id: String): Int

    fun update(id: String, dto: CronJobDto): Int

    fun create(dto: CronJobDto): String

    fun findAll(): MutableList<CronJob>

    fun findById(id: String): CronJob?

    fun findByIds(ids: List<String>): MutableList<CronJob>

    fun findBySharedCodeId(
        sharedCodeId: String,
    ): MutableList<CronJob>

    fun pageFind(pageSize: Int, pageNum: Int): List<CronJob>


}