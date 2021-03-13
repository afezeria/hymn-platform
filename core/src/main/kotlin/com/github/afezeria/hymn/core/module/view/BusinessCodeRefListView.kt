package com.github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BusinessCodeRefListView(
    var id: String,
    @ApiModelProperty(value = "触发器id")
    var byTriggerId: String? = null,
    @ApiModelProperty(value = "触发器api")
    var byTriggerApi: String? = null,
    @ApiModelProperty(value = "触发器所属对象id")
    var byTriggerObjectId: String? = null,
    @ApiModelProperty(value = "触发器所属对象api")
    var byTriggerObjectApi: String? = null,
    @ApiModelProperty(value = "触发器所属对象名称")
    var byTriggerObjectName: String? = null,
    @ApiModelProperty(value = "接口id")
    var byInterfaceId: String? = null,
    @ApiModelProperty(value = "接口名称")
    var byInterfaceName: String? = null,
    @ApiModelProperty(value = "接口api")
    var byInterfaceApi: String? = null,
    @ApiModelProperty(value = "自定义函数id")
    var byCustomFunctionId: String? = null,
    @ApiModelProperty(value = "自定义函数api")
    var byCustomFunctionApi: String? = null,
    @ApiModelProperty(value = "被引用对象id")
    var bizObjectId: String? = null,
    @ApiModelProperty(value = "被引用对象api")
    var bizObjectApi: String? = null,
    @ApiModelProperty(value = "被引用对象名称")
    var bizObjectName: String? = null,
    @ApiModelProperty(value = "被引用字段id")
    var fieldId: String? = null,
    @ApiModelProperty(value = "被引用字段api")
    var fieldApi: String? = null,
    @ApiModelProperty(value = "被引用字段名称")
    var fieldName: String? = null,
    @ApiModelProperty(value = "被引用自定义函数id")
    var customFunctionId: String? = null,
    @ApiModelProperty(value = "被引用自定义函数api")
    var customFunctionApi: String? = null,
)