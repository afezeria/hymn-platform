package com.github.afezeria.hymn.core.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 业务对象记录类型、角色和页面布局关联表
 * @author afezeria
 */
@ApiModel(
    value = "业务对象记录类型、角色和页面布局关联表",
    description = """业务对象记录类型、角色和页面布局关联表 """
)
data class BizObjectTypeLayout(

    @ApiModelProperty(value = "角色id ")
    var roleId: String,
    @ApiModelProperty(value = "业务对象id ")
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型id ")
    var typeId: String,
    @ApiModelProperty(value = "页面布局id ")
    var layoutId: String,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
