package com.github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BusinessCodeRefListView(
    var id: String,
    @ApiModelProperty(value = "远程对象id")
    var byObjectId: String? = null,
    @ApiModelProperty(value = "远程对象api")
    var byObjectApi: String? = null,
    @ApiModelProperty(value = "远程对象名称")
    var byObjectName: String? = null,
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
    var byFunctionId: String? = null,
    @ApiModelProperty(value = "自定义函数api")
    var byFunctionApi: String? = null,
    @ApiModelProperty(value = "被引用对象id")
    var refObjectId: String? = null,
    @ApiModelProperty(value = "被引用对象api")
    var refObjectApi: String? = null,
    @ApiModelProperty(value = "被引用对象名称")
    var refObjectName: String? = null,
    @ApiModelProperty(value = "被引用字段id")
    var refFieldId: String? = null,
    @ApiModelProperty(value = "被引用字段api")
    var refFieldApi: String? = null,
    @ApiModelProperty(value = "被引用字段名称")
    var refFieldName: String? = null,
    @ApiModelProperty(value = "被引用自定义函数id")
    var refFunctionId: String? = null,
    @ApiModelProperty(value = "被引用自定义函数api")
    var refFunctionApi: String? = null,
)