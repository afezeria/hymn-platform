package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 用户侧边栏菜单布局
 * @author afezeria
 */
@ApiModel(value="用户侧边栏菜单布局",description = """用户侧边栏菜单布局""")
data class AccountMenuLayout(

    @ApiModelProperty(value = "用户id ;; fk:[core_account cascade];idx")
    var accountId: String,
    @ApiModelProperty(value = "客户端类型 ;; optional_value:[browser(浏览器), mobile(移动端)]")
    var clientType: String,
    @ApiModelProperty(value = "布局json字符串")
    var layoutJson: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
