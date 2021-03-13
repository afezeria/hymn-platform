package com.github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@ApiModel(value = "模块", description = """模块""")
data class Module(
    @ApiModelProperty(value = "模块api")
    val api: String,
    @ApiModelProperty(value = "模块名称")
    val name: String,
    @ApiModelProperty(value = "模块备注")
    val remark: String,
    @ApiModelProperty(value = "版本号")
    val version: String,
    @ApiModelProperty(value = "添加时间")
    val create_date: LocalDateTime,
)
