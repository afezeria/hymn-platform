package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.CronJob
import github.afezeria.hymn.core.module.dto.CronJobDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface CronJobService {

    fun removeById(id: String): Int

    fun update(id: String, dto: CronJobDto): Int

    fun create(dto: CronJobDto): String

    fun findAll(): MutableList<CronJob>

    fun findById(id: String): CronJob?

    fun findByIds(ids: List<String>): MutableList<CronJob>

    fun findBySharedCodeId(
        sharedCodeId: String,
    ): MutableList<CronJob>


}