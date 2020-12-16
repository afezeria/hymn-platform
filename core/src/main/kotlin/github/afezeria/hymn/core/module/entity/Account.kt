package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 用户
 * @author afezeria
 */
@ApiModel(value="用户",description = """用户""")
data class Account(

    @ApiModelProperty(value = "锁定时间，当前时间小于等于lock_time表示帐号被锁定")
    var lockTime: LocalDateTime,
    @ApiModelProperty(value = "")
    var name: String,
    @ApiModelProperty(value = "")
    var username: String,
    @ApiModelProperty(value = "")
    var password: String,
    @ApiModelProperty(value = "在线规则，限制每客户端在线数量或登录ip等, none为无限制")
    var onlineRule: String,
    @ApiModelProperty(value = "是否启用")
    var active: Boolean,
    @ApiModelProperty(value = "是否是管理员")
    var admin: Boolean,
    @ApiModelProperty(value = "是否是初始帐号")
    var root: Boolean,
    @ApiModelProperty(value = "直接上级id ;;idx", required = true)
    var leaderId: String? = null,
    @ApiModelProperty(value = "所属组织id ;; fk:[core_org restrict];idx")
    var orgId: String,
    @ApiModelProperty(value = "所属角色id ;; fk:[core_role restrict];idx")
    var roleId: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
