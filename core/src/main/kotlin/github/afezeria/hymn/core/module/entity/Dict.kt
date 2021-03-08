package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 数据字典
 * @author afezeria
 */
@ApiModel(value = "数据字典", description = """数据字典""")
data class Dict(

    @ApiModelProperty(value = "表明当前字典是指定字段的字典，不能通用，通用字典可以被任意多选字段使用;idx")
    var fieldId: String? = null,
    @ApiModelProperty(value = "表明当前字典值依赖与其他字典")
    var parentDictId: String? = null,
    @ApiModelProperty(value = "字典名称")
    var name: String,
    @ApiModelProperty(value = "api名称 ")
    var api: String,
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
