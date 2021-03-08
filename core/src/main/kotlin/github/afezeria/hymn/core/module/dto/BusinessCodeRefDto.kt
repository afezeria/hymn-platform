package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BusinessCodeRefDto(
    @ApiModelProperty(value = "触发器id ;;fk:[core_biz_object_trigger cascade]")
    var triggerId: String? = null,
    @ApiModelProperty(value = "接口id ;;fk:[core_custom_interface cascade]")
    var interfaceId: String? = null,
    @ApiModelProperty(value = "自定义函数id ;;fk:[core_shared_code cascade]")
    var customFunctionId: String? = null,
    @ApiModelProperty(value = "被引用对象id ;;fk:[core_biz_object cascade]")
    var bizObjectId: String? = null,
    @ApiModelProperty(value = "被引用字段id ;;fk:[core_biz_object_field cascade];idx")
    var fieldId: String? = null,
    @ApiModelProperty(value = "被引用自定义函数id ;;fk:[core_shared_code cascade];idx")
    var refCustomFunctionId: String? = null,
) {
    fun toEntity(): BusinessCodeRef {
        return BusinessCodeRef(
            triggerId = triggerId,
            interfaceId = interfaceId,
            customFunctionId = customFunctionId,
            bizObjectId = bizObjectId,
            fieldId = fieldId,
            refCustomFunctionId = refCustomFunctionId,
        )
    }

    fun update(entity: BusinessCodeRef) {
        entity.also {
            it.triggerId = triggerId
            it.interfaceId = interfaceId
            it.customFunctionId = customFunctionId
            it.bizObjectId = bizObjectId
            it.fieldId = fieldId
            it.orgId = orgId
            it.roleId = roleId
            it.refCustomFunctionId = refCustomFunctionId
        }
    }
}
