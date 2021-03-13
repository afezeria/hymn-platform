package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.BizObjectTrigger
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTriggerDto(
    @ApiModelProperty(value = "是否启用", required = true)
    var active: Boolean,
    @ApiModelProperty(value = "")
    var remark: String? = null,
    @ApiModelProperty(value = "所属业务对象id", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "触发器名称，用于后台显示", required = true)
    var name: String,
    @ApiModelProperty(value = "api名称，用于报错显示和后台查看", required = true)
    var api: String,
    @ApiModelProperty(value = "语言 ;;optional_value:[javascript]", required = true)
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现)")
    var optionText: String? = null,
    @ApiModelProperty(value = "优先级", required = true)
    var ord: Int,
    @ApiModelProperty(
        value = "触发时间 ;;optional_value:[BEFORE_INSERT,BEFORE_UPDATE,BEFORE_UPSERT,BEFORE_DELETE,AFTER_INSERT,AFTER_UPDATE,AFTER_UPSERT,AFTER_DELETE]",
        required = true
    )
    var event: String,
    @ApiModelProperty(value = "触发器代码", required = true)
    var code: String,
) {
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
