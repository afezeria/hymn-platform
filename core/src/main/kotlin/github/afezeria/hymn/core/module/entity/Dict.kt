package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 数据字典
 * @author afezeria
 */
@ApiModel(value="数据字典",description = """数据字典""")
data class Dict(

    @ApiModelProperty(value = "表明当前字典是指定字段的字典，不能通用，通用字典可以被任意多选字段使用;idx", required = true)
    var fieldId: String? = null,
    @ApiModelProperty(value = "表明当前字典值依赖与其他字典", required = true)
    var parentDictId: String? = null,
    @ApiModelProperty(value = "字典名称")
    var name: String,
    @ApiModelProperty(value = "api名称 ;;uk")
    var api: String,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
