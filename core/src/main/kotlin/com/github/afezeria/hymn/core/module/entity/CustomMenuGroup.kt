package com.github.afezeria.hymn.core.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 用户侧边栏菜单布局
 * @author afezeria
 */
@ApiModel(value = "菜单分组", description = """菜单分组""")
data class CustomMenuGroup(

    @ApiModelProperty(value = "分组名")
    var name: String,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
