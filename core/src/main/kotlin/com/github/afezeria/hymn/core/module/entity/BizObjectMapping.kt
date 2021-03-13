package com.github.afezeria.hymn.core.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 对象映射关系 描述以一个对象的数据为基础新建其他对象的数据时字段间的映射关系，比如根据订单创建发货单时将订单中的字段映射到发货单中
 * @author afezeria
 */
@ApiModel(
    value = "对象映射关系",
    description = """对象映射关系 描述以一个对象的数据为基础新建其他对象的数据时字段间的映射关系，比如根据订单创建发货单时将订单中的字段映射到发货单中 """
)
data class BizObjectMapping(

    @ApiModelProperty(value = "源对象id ")
    var sourceBizObjectId: String,
    @ApiModelProperty(value = "源对象记录类型id ")
    var sourceTypeId: String,
    @ApiModelProperty(value = "目标对象id ")
    var targetBizObjectId: String,
    @ApiModelProperty(value = "目标对象记录类型id ")
    var targetTypeId: String,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
