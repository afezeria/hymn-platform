package com.github.afezeria.hymn.core.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 业务对象记录类型 ;; uk:[[biz_object_id name]]
 * @author afezeria
 */
@ApiModel(value = "业务对象记录类型", description = """业务对象记录类型 """)
data class BizObjectType(

    @ApiModelProperty(value = "所属业务对象id ")
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型名称")
    var name: String,
    @ApiModelProperty(
        value = "默认使用的页面布局的id "
    )
    var defaultLayoutId: String,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
