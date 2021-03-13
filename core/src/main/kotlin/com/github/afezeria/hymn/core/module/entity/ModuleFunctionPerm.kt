package com.github.afezeria.hymn.core.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 模块功能权限表
 * @author afezeria
 */
@ApiModel(value = "模块功能权限表", description = """模块功能权限表 """)
data class ModuleFunctionPerm(

    @ApiModelProperty(value = "角色id ")
    var roleId: String,
    @ApiModelProperty(value = "功能api ")
    var functionApi: String,
    @ApiModelProperty(value = "是否有访问权限")
    var perm: Boolean = false,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
