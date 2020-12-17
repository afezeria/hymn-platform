package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.AccountMenuLayout
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class AccountMenuLayoutDto(
    @ApiModelProperty(value = "用户id ;; fk:[core_account cascade];idx")
    var accountId: String,
    @ApiModelProperty(value = "客户端类型 ;; optional_value:[browser(浏览器), mobile(移动端)]")
    var clientType: String,
    @ApiModelProperty(value = "布局json字符串")
    var layoutJson: String,
){
    fun toEntity(): AccountMenuLayout {
        return AccountMenuLayout(
            accountId = accountId,
            clientType = clientType,
            layoutJson = layoutJson,
        )
    }

    fun update(entity: AccountMenuLayout) {
        entity.also {
            it.accountId = accountId
            it.clientType = clientType
            it.layoutJson = layoutJson
        }
    }
}
