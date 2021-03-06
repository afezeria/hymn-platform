package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.BizObjectMapping
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectMappingDto(
    @ApiModelProperty(value = "源对象id", required = true)
    var sourceBizObjectId: String,
    @ApiModelProperty(value = "源对象记录类型id", required = true)
    var sourceTypeId: String,
    @ApiModelProperty(value = "目标对象id", required = true)
    var targetBizObjectId: String,
    @ApiModelProperty(value = "目标对象记录类型id", required = true)
    var targetTypeId: String,
) {

    @ApiModelProperty(value = "目标对象记录类型名称", notes = "只用于后端返回数据")
    var targetTypeName: String? = null

    fun toEntity(): BizObjectMapping {
        return BizObjectMapping(
            sourceBizObjectId = sourceBizObjectId,
            sourceTypeId = sourceTypeId,
            targetBizObjectId = targetBizObjectId,
            targetTypeId = targetTypeId,
        )
    }

    fun update(entity: BizObjectMapping) {
        entity.also {
            it.sourceBizObjectId = sourceBizObjectId
            it.sourceTypeId = sourceTypeId
            it.targetBizObjectId = targetBizObjectId
            it.targetTypeId = targetTypeId
        }
    }
}
