package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 对象权限 ;;uk:[[role_id biz_object_id]]
 * @author afezeria
 */
@ApiModel(value="对象权限",description = """对象权限 ;;uk:[[role_id biz_object_id]]""")
data class BizObjectPerm(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "对象id ;;fk:[core_biz_object cascade];idx")
    var bizObjectId: String,
    @ApiModelProperty(value = "创建")
    var ins: Boolean,
    @ApiModelProperty(value = "更新")
    var upd: Boolean,
    @ApiModelProperty(value = "删除")
    var del: Boolean,
    @ApiModelProperty(value = "查看")
    var que: Boolean,
    @ApiModelProperty(value = "查看本人及直接下属")
    var queryWithAccountTree: Boolean,
    @ApiModelProperty(value = "查看本部门")
    var queryWithDept: Boolean,
    @ApiModelProperty(value = "查看本部门及下级部门")
    var queryWithDeptTree: Boolean,
    @ApiModelProperty(value = "查看全部")
    var queryAll: Boolean,
    @ApiModelProperty(value = "编辑全部")
    var editAll: Boolean,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
