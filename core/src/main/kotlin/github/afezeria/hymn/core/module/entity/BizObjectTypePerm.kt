package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 记录类型权限 ;;uk:[[role_id type_id]]
 * @author afezeria
 */
@ApiModel(value="记录类型权限",description = """记录类型权限 ;;uk:[[role_id type_id]]""")
data class BizObjectTypePerm(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade]")
    var roleId: String,
    @ApiModelProperty(value = "类型id ;;fk:[core_biz_object_type cascade];idx")
    var typeId: String,
    @ApiModelProperty(value = "创建数据时选择特定记录类型的权限")
    var visible: Boolean,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
