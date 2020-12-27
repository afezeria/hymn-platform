package github.afezeria.hymn.core.module.entity

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

    @ApiModelProperty(value = "是否启用", required = true)
    var active: Boolean,
    @ApiModelProperty(value = "任务代码id ;;fk:[core_shared_code restrict];idx", required = true)
    var sharedCodeId: String,
    @ApiModelProperty(value = "定时规则", required = true)
    var cron: String,
    @ApiModelProperty(value = "任务开始时间", required = true)
    var startDateTime: LocalDateTime,
    @ApiModelProperty(value = "任务结束时间", required = true)
    var endDateTime: LocalDateTime,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
