package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@ApiModel(value = "模块", description = """模块""")
data class ModuleFunction(
    @ApiModelProperty(value = "功能api")
    val api: String,
    @ApiModelProperty(value = "功能名称")
    val name: String,
    @ApiModelProperty(value = "模块api")
    val moduleApi: String,
    @ApiModelProperty(value = "")
    val remark: String,
    @ApiModelProperty(value = "添加时间")
    val create_date: LocalDateTime,
)
