package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 字段权限 ;;uk:[[role_id field_id]]
 * @author afezeria
 */
@ApiModel(value = "字段权限", description = """字段权限 ;;uk:[[role_id field_id]]""")
data class BizObjectFieldPerm(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(value = "字段id ;;fk:[core_biz_object_field cascade];idx", required = true)
    var fieldId: String,
    @ApiModelProperty(value = "可读", required = true)
    var pRead: Boolean,
    @ApiModelProperty(value = "可编辑", required = true)
    var pEdit: Boolean,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
