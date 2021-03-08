package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.AccountMenuLayout
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class AccountMenuLayoutDto(
    @ApiModelProperty(value = "用户id", required = true)
    var accountId: String,
    @ApiModelProperty(
        value = "客户端类型 ;; optional_value:[browser(浏览器), mobile(移动端)]",
        required = true
    )
    var clientType: String,
    @ApiModelProperty(value = "布局json字符串", required = true)
    var layoutJson: String,
) {
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
