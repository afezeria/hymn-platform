package github.afezeria.hymn.oss.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 * 创建文件预签url记录，预签url不需要任何权限验证
 * @author afezeria
 */
@ApiModel(value="创建文件预签url记录，预签url不需要任何权限验证",description = """创建文件预签url记录，预签url不需要任何权限验证""")
data class PreSignedHistory(

    @ApiModelProperty(value = "文件id ;; idx", required = true)
    var fileId: String,
    @ApiModelProperty(value = "有效时间，单位：秒", required = true)
    var expiry: Int,
) {

    lateinit var id: String
    lateinit var createDate: LocalDateTime
    lateinit var createById: String
    lateinit var createBy: String

}
