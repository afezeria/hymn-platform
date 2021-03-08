package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 定时任务
 * @author afezeria
 */
@ApiModel(value = "定时任务", description = """定时任务""")
data class CronJob(

    @ApiModelProperty(value = "是否启用")
    var active: Boolean,
    @ApiModelProperty(value = "任务代码id ")
    var sharedCodeId: String,
    @ApiModelProperty(value = "定时规则")
    var cron: String,
    @ApiModelProperty(value = "任务开始时间")
    var startDateTime: LocalDateTime,
    @ApiModelProperty(value = "任务结束时间")
    var endDateTime: LocalDateTime,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
