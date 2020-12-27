package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.Account
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author afezeria
 */
class AccountDto(
    @ApiModelProperty(value = "锁定时间，当前时间小于等于lock_time表示帐号被锁定", required = true)
    var lockTime: LocalDateTime,
    @ApiModelProperty(value = "", required = true)
    var name: String,
    @ApiModelProperty(value = "", required = true)
    var username: String,
    @ApiModelProperty(value = "", required = true)
    var password: String,
    @ApiModelProperty(value = "在线规则，限制每客户端在线数量或登录ip等, none为无限制", required = true)
    var onlineRule: String,
    @ApiModelProperty(value = "是否启用", required = true)
    var active: Boolean,
    @ApiModelProperty(value = "是否是管理员", required = true)
    var admin: Boolean,
    @ApiModelProperty(value = "是否是初始帐号", required = true)
    var root: Boolean,
    @ApiModelProperty(value = "直接上级id ;;idx")
    var leaderId: String? = null,
    @ApiModelProperty(value = "所属组织id ;; fk:[core_org restrict];idx", required = true)
    var orgId: String,
    @ApiModelProperty(value = "所属角色id ;; fk:[core_role restrict];idx", required = true)
    var roleId: String,
) {
    fun toEntity(): Account {
        return Account(
            lockTime = lockTime,
            name = name,
            username = username,
            password = password,
            onlineRule = onlineRule,
            active = active,
            admin = admin,
            root = root,
            leaderId = leaderId,
            orgId = orgId,
            roleId = roleId,
        )
    }

    fun update(entity: Account) {
        entity.also {
            it.lockTime = lockTime
            it.name = name
            it.username = username
            it.password = password
            it.onlineRule = onlineRule
            it.active = active
            it.admin = admin
            it.root = root
            it.leaderId = leaderId
            it.orgId = orgId
            it.roleId = roleId
        }
    }
}
