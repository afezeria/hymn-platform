package com.github.afezeria.hymn.core.module.view

import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectFieldPermListView(
    @ApiModelProperty(value = "角色id")
    var roleId: String,
    @ApiModelProperty(value = "字段id ")
    var fieldId: String,
    @ApiModelProperty(value = "角色名称")
    var roleName: String,
    @ApiModelProperty(value = "字段所属对象id")
    var bizObjectId: String,
    @ApiModelProperty(value = "字段名称")
    var fieldName: String,
    @ApiModelProperty(value = "可读")
    var pRead: Boolean = false,
    @ApiModelProperty(value = "可编辑")
    var pEdit: Boolean = false,
)