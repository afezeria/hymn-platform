package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectTrigger
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class BizObjectTriggerDto(
    @ApiModelProperty(value = "是否启用")
    var active: Boolean,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
    @ApiModelProperty(value = "所属业务对象id")
    var bizObjectId: String,
    @ApiModelProperty(value = "触发器名称，用于后台显示")
    var name: String,
    @ApiModelProperty(value = "api名称，用于报错显示和后台查看")
    var api: String,
    @ApiModelProperty(value = "语言 ;;optional_value:[javascript]")
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）", required = true)
    var optionText: String? = null,
    @ApiModelProperty(value = "优先级")
    var ord: Int,
    @ApiModelProperty(value = "触发时间 ;;optional_value:[BEFORE_INSERT,BEFORE_UPDATE,BEFORE_UPSERT,BEFORE_DELETE,AFTER_INSERT,AFTER_UPDATE,AFTER_UPSERT,AFTER_DELETE]")
    var event: String,
    @ApiModelProperty(value = "触发器代码")
    var code: String,
){
    fun toEntity(): BizObjectTrigger {
        return BizObjectTrigger(
            active = active,
            remark = remark,
            bizObjectId = bizObjectId,
            name = name,
            api = api,
            lang = lang,
            optionText = optionText,
            ord = ord,
            event = event,
            code = code,
        )
    }

    fun fromEntity(entity: BizObjectTrigger): BizObjectTriggerDto {
        return entity.run {
            BizObjectTriggerDto(
                active = active,
                remark = remark,
                bizObjectId = bizObjectId,
                name = name,
                api = api,
                lang = lang,
                optionText = optionText,
                ord = ord,
                event = event,
                code = code,
          )
        }
    }

    fun update(entity: BizObjectTrigger) {
        entity.also {
            it.active = active
            it.remark = remark
            it.bizObjectId = bizObjectId
            it.name = name
            it.api = api
            it.lang = lang
            it.optionText = optionText
            it.ord = ord
            it.event = event
            it.code = code
        }
    }
}
