package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 用户业务对象列表视图
 * @author afezeria
 */
@ApiModel(value = "用户业务对象列表视图", description = """用户业务对象列表视图""")
data class AccountObjectView(

    @ApiModelProperty(value = "源数据id，修改视图后该字段置空;idx")
    var copyId: String? = null,
    @ApiModelProperty(value = "")
    var remark: String? = null,
    @ApiModelProperty(value = "是否所有人可见", required = true)
    var globalView: Boolean,
    @ApiModelProperty(value = "是否是默认视图，只有管理员可以设置", required = true)
    var defaultView: Boolean,
    @ApiModelProperty(value = "所属用户id ;; fk:[core_account cascade];idx", required = true)
    var accountId: String,
    @ApiModelProperty(value = "所属对象id ;; fk:[core_biz_object cascade];idx", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "视图名称", required = true)
    var name: String,
    @ApiModelProperty(value = "视图结构", required = true)
    var viewJson: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
