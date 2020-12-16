package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 模块列表
 * @author afezeria
 */
@ApiModel(value="模块列表",description = """模块列表""")
data class Module(

    @ApiModelProperty(value = "模块api ;;uk", required = true)
    var api: String? = null,
    @ApiModelProperty(value = "模块名称")
    var name: String,
    @ApiModelProperty(value = "")
    var remark: String,
    @ApiModelProperty(value = "")
    var version: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
