package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.BizObjectType
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypeDto(
    @ApiModelProperty(value = "所属业务对象id", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型名称", required = true)
    var name: String,
    @ApiModelProperty(
        value = "默认使用的页面布局的id",
        required = true
    )
    var defaultLayoutId: String,
    @ApiModelProperty(value = "是否启用", required = true)
    var active: Boolean,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) {
    fun toEntity(): BizObjectType {
        return BizObjectType(
            bizObjectId = bizObjectId,
            name = name,
            defaultLayoutId = defaultLayoutId,
            remark = remark,
        )
    }

    fun update(entity: BizObjectType) {
        entity.also {
            it.bizObjectId = bizObjectId
            it.name = name
            it.defaultLayoutId = defaultLayoutId
            it.remark = remark
        }
    }
}
