package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.CronJob
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author afezeria
 */
class CronJobDto(
    @ApiModelProperty(value = "是否启用", required = true)
    var active: Boolean,
    @ApiModelProperty(value = "任务代码id", required = true)
    var sharedCodeId: String,
    @ApiModelProperty(value = "定时规则", required = true)
    var cron: String,
    @ApiModelProperty(value = "任务开始时间", required = true)
    var startDateTime: LocalDateTime,
    @ApiModelProperty(value = "任务结束时间", required = true)
    var endDateTime: LocalDateTime,
) {
    fun toEntity(): CronJob {
        return CronJob(
            active = active,
            sharedCodeId = sharedCodeId,
            cron = cron,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
        )
    }

    fun update(entity: CronJob) {
        entity.also {
            it.active = active
            it.sharedCodeId = sharedCodeId
            it.cron = cron
            it.startDateTime = startDateTime
            it.endDateTime = endDateTime
        }
    }
}
