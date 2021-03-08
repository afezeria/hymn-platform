package github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BusinessCodeRefListView(
    var id: String,
    @ApiModelProperty(value = "触发器id ;;fk:[core_biz_object_trigger cascade]")
    var byTriggerId: String? = null,
    var byTriggerApi: String? = null,
    var byTriggerObjectId: String? = null,
    var byTriggerObjectApi: String? = null,
    var byTriggerObjectName: String? = null,
    @ApiModelProperty(value = "接口id ;;fk:[core_custom_interface cascade]")
    var byInterfaceId: String? = null,
    var byInterfaceName: String? = null,
    var byInterfaceApi: String? = null,
    @ApiModelProperty(value = "自定义函数id ;;fk:[core_shared_code cascade]")
    var byCustomFunctionId: String? = null,
    var byCustomFunctionApi: String? = null,
    @ApiModelProperty(value = "被引用对象id ;;fk:[core_biz_object cascade]")
    var bizObjectId: String? = null,
    var bizObjectApi: String? = null,
    var bizObjectName: String? = null,
    @ApiModelProperty(value = "被引用字段id ;;fk:[core_biz_object_field cascade];idx")
    var fieldId: String? = null,
    var fieldApi: String? = null,
    var fieldName: String? = null,
    @ApiModelProperty(value = "被引用自定义函数id ;;fk:[core_shared_code cascade];idx")
    var customFunctionId: String? = null,
    var customFunctionApi: String? = null,
)