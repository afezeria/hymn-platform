package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@ApiModel(value = "模块", description = """模块""")
data class Module(
    @ApiModelProperty(value = "模块api", required = true)
    val api: String,
    @ApiModelProperty(value = "模块名称", required = true)
    val name: String,
    @ApiModelProperty(value = "模块备注", required = true)
    val remark: String,
    @ApiModelProperty(value = "版本号", required = true)
    val version: String,
    @ApiModelProperty(value = "添加时间", required = true)
    val create_date: LocalDateTime,
)
