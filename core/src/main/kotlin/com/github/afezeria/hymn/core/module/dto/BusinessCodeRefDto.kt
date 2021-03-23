package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.BusinessCodeRef
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BusinessCodeRefDto(
    @ApiModelProperty(value = "远程对象id")
    var byObjectId: String? = null,
    @ApiModelProperty(value = "触发器id")
    var byTriggerId: String? = null,
    @ApiModelProperty(value = "接口id")
    var byInterfaceId: String? = null,
    @ApiModelProperty(value = "自定义函数id")
    var byFunctionId: String? = null,
    @ApiModelProperty(value = "被引用对象id")
    var refObjectId: String? = null,
    @ApiModelProperty(value = "被引用字段id")
    var refFieldId: String? = null,
    @ApiModelProperty(value = "被引用自定义函数id")
    var refFunctionId: String? = null,
) {
    fun toEntity(): BusinessCodeRef {
        return BusinessCodeRef(
            byObjectId = byObjectId,
            byTriggerId = byTriggerId,
            byApiId = byInterfaceId,
            byFunctionId = byFunctionId,
            refObjectId = refObjectId,
            refFieldId = refFieldId,
            refFunctionId = refFunctionId,
        )
    }

    fun update(entity: BusinessCodeRef) {
        entity.also {
            it.byTriggerId = byTriggerId
            it.byApiId = byInterfaceId
            it.byFunctionId = byFunctionId
            it.refObjectId = refObjectId
            it.refFieldId = refFieldId
            it.refFunctionId = refFunctionId
        }
    }
}
