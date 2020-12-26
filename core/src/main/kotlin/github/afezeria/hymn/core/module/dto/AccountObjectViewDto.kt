package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.AccountObjectView
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class AccountObjectViewDto(
    @ApiModelProperty(value = "源数据id，修改视图后该字段置空;idx", required = true)
    var copyId: String? = null,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
    @ApiModelProperty(value = "是否所有人可见")
    var globalView: Boolean,
    @ApiModelProperty(value = "是否是默认视图，只有管理员可以设置")
    var defaultView: Boolean,
    @ApiModelProperty(value = "所属用户id ;; fk:[core_account cascade];idx")
    var accountId: String,
    @ApiModelProperty(value = "所属对象id ;; fk:[core_biz_object cascade];idx")
    var bizObjectId: String,
    @ApiModelProperty(value = "视图名称")
    var name: String,
    @ApiModelProperty(value = "视图结构")
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
