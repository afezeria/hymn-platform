package com.github.afezeria.hymn.core.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 组织
 * @author afezeria
 */
@ApiModel(value = "组织", description = """组织""")
data class Org(

    @ApiModelProperty(value = "")
    var name: String,
    @ApiModelProperty(value = "部门领导id")
    var directorId: String? = null,
    @ApiModelProperty(value = "部门副领导id")
    var deputyDirectorId: String? = null,
    @ApiModelProperty(value = "上级组织id ")
    var parentId: String? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
