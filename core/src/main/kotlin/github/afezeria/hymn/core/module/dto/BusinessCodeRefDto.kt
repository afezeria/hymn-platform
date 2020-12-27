package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BusinessCodeRefDto(
    @ApiModelProperty(value = "触发器id ;;fk:[core_biz_object_trigger cascade]", required = true)
    var triggerId: String? = null,
    @ApiModelProperty(value = "接口id ;;fk:[core_custom_interface cascade]", required = true)
    var interfaceId: String? = null,
    @ApiModelProperty(value = "共享代码id ;;fk:[core_shared_code cascade]", required = true)
    var sharedCodeId: String? = null,
    @ApiModelProperty(value = "被引用对象id ;;fk:[core_biz_object cascade]", required = true)
    var bizObjectId: String? = null,
    @ApiModelProperty(value = "被引用字段id ;;fk:[core_biz_object_field cascade];idx", required = true)
    var fieldId: String? = null,
    @ApiModelProperty(value = "被引用组织id ;;fk:[core_org cascade];idx", required = true)
    var orgId: String? = null,
    @ApiModelProperty(value = "被引用角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String? = null,
    @ApiModelProperty(value = "被引用共享代码id ;;fk:[core_shared_code cascade];idx", required = true)
    var refSharedCodeId: String? = null,
) {
    fun toEntity(): BusinessCodeRef {
        return BusinessCodeRef(
            triggerId = triggerId,
            interfaceId = interfaceId,
            sharedCodeId = sharedCodeId,
            bizObjectId = bizObjectId,
            fieldId = fieldId,
            orgId = orgId,
            roleId = roleId,
            refSharedCodeId = refSharedCodeId,
        )
    }

    fun update(entity: BusinessCodeRef) {
        entity.also {
            it.triggerId = triggerId
            it.interfaceId = interfaceId
            it.sharedCodeId = sharedCodeId
            it.bizObjectId = bizObjectId
            it.fieldId = fieldId
            it.orgId = orgId
            it.roleId = roleId
            it.refSharedCodeId = refSharedCodeId
        }
    }
}
