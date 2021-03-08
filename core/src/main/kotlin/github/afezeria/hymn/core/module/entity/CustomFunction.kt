package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 共享代码 可以在接口、触发器中调用或使用在定时任务中
 * @author afezeria
 */
@ApiModel(value = "共享代码", description = """共享代码 可以在接口、触发器中调用或使用在定时任务中""")
data class CustomFunction(

    @ApiModelProperty(value = "api名称,也是代码中的函数名称 ;;uk", required = true)
    var api: String,
    @ApiModelProperty(value = "代码类型 ;;optional_value:[function(函数代码),job(任务代码)]", required = true)
    var type: String,
    @ApiModelProperty(value = "代码", required = true)
    var code: String,
    @ApiModelProperty(value = "参数类型数组，多个类型之间用英文逗号隔开，类型为java类型的全限定名", required = true)
    var paramsType: String,
    @ApiModelProperty(value = "语言 ;;optional_value:[javascript]", required = true)
    var lang: String,
    @ApiModelProperty(value = "用于给编译器或其他组件设置参数(格式参照具体实现）")
    var optionText: String? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
