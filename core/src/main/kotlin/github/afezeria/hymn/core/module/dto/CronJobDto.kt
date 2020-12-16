package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.CronJob
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class CronJobDto(
    @ApiModelProperty(value = "是否启用")
    var active: Boolean,
    @ApiModelProperty(value = "任务代码id ;;fk:[core_shared_code restrict];idx")
    var sharedCodeId: String,
    @ApiModelProperty(value = "定时规则")
    var cron: String,
    @ApiModelProperty(value = "任务开始时间")
    var startDateTime: LocalDateTime,
    @ApiModelProperty(value = "任务结束时间")
    var endDateTime: LocalDateTime,
){
    fun toEntity(): CronJob {
        return CronJob(
            active = active,
            sharedCodeId = sharedCodeId,
            cron = cron,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
        )
    }

    fun fromEntity(entity: CronJob): CronJobDto {
        return entity.run {
            CronJobDto(
                active = active,
                sharedCodeId = sharedCodeId,
                cron = cron,
                startDateTime = startDateTime,
                endDateTime = endDateTime,
          )
        }
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
