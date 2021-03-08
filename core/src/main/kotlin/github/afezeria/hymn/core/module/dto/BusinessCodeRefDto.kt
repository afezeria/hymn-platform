package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BusinessCodeRefDto(
    @ApiModelProperty(value = "触发器id")
    var byTriggerId: String? = null,
    @ApiModelProperty(value = "接口id")
    var byInterfaceId: String? = null,
    @ApiModelProperty(value = "自定义函数id")
    var byCustomFunctionId: String? = null,
    @ApiModelProperty(value = "被引用对象id")
    var bizObjectId: String? = null,
    @ApiModelProperty(value = "被引用字段id")
    var fieldId: String? = null,
    @ApiModelProperty(value = "被引用自定义函数id")
    var customFunctionId: String? = null,
) {
    fun toEntity(): BusinessCodeRef {
        return BusinessCodeRef(
            byTriggerId = byTriggerId,
            byInterfaceId = byInterfaceId,
            byCustomFunctionId = byCustomFunctionId,
            bizObjectId = bizObjectId,
            fieldId = fieldId,
            customFunctionId = customFunctionId,
        )
    }

    fun update(entity: BusinessCodeRef) {
        entity.also {
            it.byTriggerId = byTriggerId
            it.byInterfaceId = byInterfaceId
            it.byCustomFunctionId = byCustomFunctionId
            it.bizObjectId = bizObjectId
            it.fieldId = fieldId
            it.customFunctionId = customFunctionId
        }
    }
}
