package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 系统配置表
 * @author afezeria
 */
@ApiModel(value = "系统配置表", description = """系统配置表""")
data class Config(

    @ApiModelProperty(value = "键 ")
    var key: String,
    @ApiModelProperty(value = "")
    var value: String,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
