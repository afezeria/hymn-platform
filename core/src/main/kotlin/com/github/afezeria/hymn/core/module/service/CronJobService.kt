package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.CronJobDto
import com.github.afezeria.hymn.core.module.entity.CronJob

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

    fun pageFind(pageSize: Int, pageNum: Int): List<CronJob>
}