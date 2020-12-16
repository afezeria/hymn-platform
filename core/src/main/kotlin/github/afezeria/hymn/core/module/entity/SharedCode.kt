package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 共享代码 可以在接口、触发器中调用或使用在定时任务中
 * @author afezeria
 */
@ApiModel(value="共享代码",description = """共享代码 可以在接口、触发器中调用或使用在定时任务中""")
data class SharedCode(

    @ApiModelProperty(value = "api名称,也是代码中的函数名称 ;;uk")
    var api: String,
    @ApiModelProperty(value = "代码类型 ;;optional_value:[function(函数代码),job(任务代码)]")
    var type: String,
    @ApiModelProperty(value = "代码")
    var code: String,
    @ApiModelProperty(value = "语言 ;;optional_value:[javascript]")
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）", required = true)
    var optionText: String? = null,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
