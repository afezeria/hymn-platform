package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 角色
 * @author afezeria
 */
@ApiModel(value = "角色", description = """角色""")
data class Role(

    @ApiModelProperty(value = "角色名称")
    var name: String,
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
