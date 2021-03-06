package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.BizObjectPerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectPermDto(
    @ApiModelProperty(value = "角色id", required = true)
    var roleId: String,
    @ApiModelProperty(
        value = "对象id，新建对象时该字段用空字符串占位",
        required = true
    )
    var bizObjectId: String,
    @ApiModelProperty(value = "创建")
    var ins: Boolean = false,
    @ApiModelProperty(value = "更新")
    var upd: Boolean = false,
    @ApiModelProperty(value = "删除")
    var del: Boolean = false,
    @ApiModelProperty(value = "查看")
    var que: Boolean = false,
    @ApiModelProperty(value = "查看本人及直接下属")
    var queryWithAccountTree: Boolean = false,
    @ApiModelProperty(value = "查看本部门")
    var queryWithOrg: Boolean = false,
    @ApiModelProperty(value = "查看本部门及下级部门")
    var queryWithOrgTree: Boolean = false,
    @ApiModelProperty(value = "查看全部")
    var queryAll: Boolean = false,
    @ApiModelProperty(value = "编辑全部")
    var editAll: Boolean = false,
) {

    fun toEntity(): BizObjectPerm {
        return BizObjectPerm(
            roleId = roleId,
            bizObjectId = bizObjectId,
            ins = ins,
            upd = upd,
            del = del,
            que = que,
            queryWithAccountTree = queryWithAccountTree,
            queryWithOrg = queryWithOrg,
            queryWithOrgTree = queryWithOrgTree,
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
            it.queryWithOrg = queryWithOrg
            it.queryWithOrgTree = queryWithOrgTree
            it.queryAll = queryAll
            it.editAll = editAll
        }
    }
}
