package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 触发器 ;;uk:[[biz_object_id api]]
 * @author afezeria
 */
@ApiModel(value="触发器",description = """触发器 ;;uk:[[biz_object_id api]]""")
data class BizObjectTrigger(

    @ApiModelProperty(value = "是否启用")
    var active: Boolean,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
    @ApiModelProperty(value = "所属业务对象id")
    var bizObjectId: String,
    @ApiModelProperty(value = "触发器名称，用于后台显示")
    var name: String,
    @ApiModelProperty(value = "api名称，用于报错显示和后台查看")
    var api: String,
    @ApiModelProperty(value = "语言 ;;optional_value:[javascript]")
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）", required = true)
    var optionText: String? = null,
    @ApiModelProperty(value = "优先级")
    var ord: Int,
    @ApiModelProperty(value = "触发时间 ;;optional_value:[BEFORE_INSERT,BEFORE_UPDATE,BEFORE_UPSERT,BEFORE_DELETE,AFTER_INSERT,AFTER_UPDATE,AFTER_UPSERT,AFTER_DELETE]")
    var event: String,
    @ApiModelProperty(value = "触发器代码")
    var code: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
