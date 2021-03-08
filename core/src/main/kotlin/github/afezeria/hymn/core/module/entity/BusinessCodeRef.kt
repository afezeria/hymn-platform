package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 业务代码引用关系表
 * @author afezeria
 */
@ApiModel(value = "业务代码引用关系表", description = """业务代码引用关系表""")
data class BusinessCodeRef(

    @ApiModelProperty(value = "触发器id ")
    var byTriggerId: String? = null,
    @ApiModelProperty(value = "接口id ")
    var byInterfaceId: String? = null,
    @ApiModelProperty(value = "自定义函数id ")
    var byCustomFunctionId: String? = null,
    @ApiModelProperty(value = "被引用对象id ")
    var bizObjectId: String? = null,
    @ApiModelProperty(value = "被引用字段id ")
    var fieldId: String? = null,
    @ApiModelProperty(value = "被引用自定义函数id ")
    var customFunctionId: String? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
