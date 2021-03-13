package com.github.afezeria.hymn.core.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 字典项
 * @author afezeria
 */
@ApiModel(value = "字典项", description = """字典项 """)
data class DictItem(

    @ApiModelProperty(value = "所属字典id")
    var dictId: String,
    @ApiModelProperty(value = "字典项名称")
    var name: String,
    @ApiModelProperty(value = "字典项编码")
    var code: String,
    @ApiModelProperty(value = "父字典中的字典项编码，用于表示多个选项列表的级联关系")
    var parentCode: String? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
