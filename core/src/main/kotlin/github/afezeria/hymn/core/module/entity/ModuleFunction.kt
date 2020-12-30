package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@ApiModel(value = "模块", description = """模块""")
data class ModuleFunction(
    @ApiModelProperty(value = "功能api", required = true)
    val api: String,
    @ApiModelProperty(value = "功能名称", required = true)
    val name: String,
    @ApiModelProperty(value = "模块api", required = true)
    val moduleApi: String,
    @ApiModelProperty(value = "", required = true)
    val remark: String,
    @ApiModelProperty(value = "添加时间", required = true)
    val create_date: LocalDateTime,
)
