package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 对象权限 ;;uk:[[role_id biz_object_id]]
 * @author afezeria
 */
@ApiModel(value = "对象权限", description = """对象权限 ;;uk:[[role_id biz_object_id]]""")
data class BizObjectPerm(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(value = "对象id ;;fk:[core_biz_object cascade];idx", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "创建", required = true)
    var ins: Boolean,
    @ApiModelProperty(value = "更新", required = true)
    var upd: Boolean,
    @ApiModelProperty(value = "删除", required = true)
    var del: Boolean,
    @ApiModelProperty(value = "查看", required = true)
    var que: Boolean,
    @ApiModelProperty(value = "查看本人及直接下属", required = true)
    var queryWithAccountTree: Boolean,
    @ApiModelProperty(value = "查看本部门", required = true)
    var queryWithOrg: Boolean,
    @ApiModelProperty(value = "查看本部门及下级部门", required = true)
    var queryWithOrgTree: Boolean,
    @ApiModelProperty(value = "查看全部", required = true)
    var queryAll: Boolean,
    @ApiModelProperty(value = "编辑全部", required = true)
    var editAll: Boolean,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
