package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 自定义页面
 * @author afezeria
 */
@ApiModel(value = "自定义页面", description = """自定义页面，上传压缩包，解压后存放在工作目录的static-resource/{api}目录下,访问路径为 /module/core/public/custom/{api}/{filename}""")
data class CustomPage(

    @ApiModelProperty(value = "api名称，唯一标识 ;;uk", required = true)
    var api: String,
    @ApiModelProperty(value = "自定义页面名称，用于后台查看", required = true)
    var name: String,
    @ApiModelProperty(value = "")
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
