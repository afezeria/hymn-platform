package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 模块功能权限表 ;;uk:[[role_id module_api function_api]]
 * @author afezeria
 */
@ApiModel(value="模块功能权限表",description = """模块功能权限表 ;;uk:[[role_id module_api function_api]]""")
data class ModuleFunctionPerm(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "模块api ;;fk:[core_module cascade]")
    var moduleApi: String,
    @ApiModelProperty(value = "功能api ;;fk:[core_module_function cascade]")
    var functionApi: String,
    @ApiModelProperty(value = "是否有访问权限", required = true)
    var perm: Boolean? = null,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
