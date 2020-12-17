package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectPerm
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class BizObjectPermDto(
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
){
    fun toEntity(): BizObjectPerm {
        return BizObjectPerm(
            roleId = roleId,
            bizObjectId = bizObjectId,
            ins = ins,
            upd = upd,
            del = del,
            que = que,
            queryWithAccountTree = queryWithAccountTree,
            queryWithDept = queryWithDept,
            queryWithDeptTree = queryWithDeptTree,
            queryAll = queryAll,
            editAll = editAll,
        )
    }

    fun update(entity: BizObjectPerm) {
        entity.also {
            it.roleId = roleId
            it.bizObjectId = bizObjectId
            it.ins = ins
            it.upd = upd
            it.del = del
            it.que = que
            it.queryWithAccountTree = queryWithAccountTree
            it.queryWithDept = queryWithDept
            it.queryWithDeptTree = queryWithDeptTree
            it.queryAll = queryAll
            it.editAll = editAll
        }
    }
}
