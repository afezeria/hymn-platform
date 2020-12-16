package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 字段权限 ;;uk:[[role_id field_id]]
 * @author afezeria
 */
@ApiModel(value="字段权限",description = """字段权限 ;;uk:[[role_id field_id]]""")
data class BizObjectFieldPerm(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "对象id ;;fk:[core_biz_object cascade]")
    var bizObjectId: String,
    @ApiModelProperty(value = "字段id ;;fk:[core_biz_object_field cascade];idx")
    var fieldId: String,
    @ApiModelProperty(value = "可读")
    var pRead: Boolean,
    @ApiModelProperty(value = "可编辑")
    var pEdit: Boolean,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
