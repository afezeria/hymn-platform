package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 按钮权限
 * @author afezeria
 */
@ApiModel(value = "按钮权限", description = """按钮权限 """)
data class ButtonPerm(

    @ApiModelProperty(value = "角色id ")
    var roleId: String,
    @ApiModelProperty(value = "按钮id ")
    var buttonId: String,
    @ApiModelProperty(value = "是否可见")
    var visible: Boolean,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
