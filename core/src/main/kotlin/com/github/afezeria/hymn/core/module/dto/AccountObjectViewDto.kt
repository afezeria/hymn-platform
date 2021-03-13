package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.AccountObjectView
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class AccountObjectViewDto(
    @ApiModelProperty(value = "源数据id，修改视图后该字段置空;idx")
    var copyId: String? = null,
    @ApiModelProperty(value = "")
    var remark: String? = null,
    @ApiModelProperty(value = "是否所有人可见", required = true)
    var globalView: Boolean,
    @ApiModelProperty(value = "是否是默认视图，只有管理员可以设置", required = true)
    var defaultView: Boolean,
    @ApiModelProperty(value = "所属用户id", required = true)
    var accountId: String,
    @ApiModelProperty(value = "所属对象id", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "视图名称", required = true)
    var name: String,
    @ApiModelProperty(value = "视图结构", required = true)
    var viewJson: String,
) {
    fun toEntity(): AccountObjectView {
        return AccountObjectView(
            copyId = copyId,
            remark = remark,
            globalView = globalView,
            defaultView = defaultView,
            accountId = accountId,
            bizObjectId = bizObjectId,
            name = name,
            viewJson = viewJson,
        )
    }

    fun update(entity: AccountObjectView) {
        entity.also {
            it.copyId = copyId
            it.remark = remark
            it.globalView = globalView
            it.defaultView = defaultView
            it.accountId = accountId
            it.bizObjectId = bizObjectId
            it.name = name
            it.viewJson = viewJson
        }
    }
}
