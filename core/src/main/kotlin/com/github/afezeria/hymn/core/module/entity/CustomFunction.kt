package com.github.afezeria.hymn.core.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 共享代码 可以在接口、触发器中调用或使用在定时任务中
 * @author afezeria
 */
@ApiModel(value = "共享代码", description = """共享代码 可以在接口、触发器中调用或使用在定时任务中""")
data class CustomFunction(

    @ApiModelProperty(value = "api名称,也是代码中的函数名称 ")
    var api: String,
    @ApiModelProperty(value = "代码类型 ;;optional_value:[function(函数代码),job(任务代码)]")
    var type: String,
    @ApiModelProperty(value = "代码")
    var code: String,
    @ApiModelProperty(value = "是否是基础函数，只有基础函数能够被其他脚本代码引用，并且基础函数不能引用其他自定义函数")
    var baseFun: Boolean = true,
    @ApiModelProperty(value = "语言 ;;optional_value:[javascript]")
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）")
    var optionText: String? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
