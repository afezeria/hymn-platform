package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 业务对象记录类型 ;; uk:[[biz_object_id name]]
 * @author afezeria
 */
@ApiModel(value="业务对象记录类型",description = """业务对象记录类型 ;; uk:[[biz_object_id name]]""")
data class BizObjectType(

    @ApiModelProperty(value = "所属业务对象id ;;fk:[core_biz_object cascade]")
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型名称")
    var name: String,
    @ApiModelProperty(value = "是否启用")
    var active: Boolean,
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
