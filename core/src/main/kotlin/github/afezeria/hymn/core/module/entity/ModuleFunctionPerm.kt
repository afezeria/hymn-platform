package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 模块功能权限表 ;;uk:[[role_id module_api function_api]]
 * @author afezeria
 */
@ApiModel(value = "模块功能权限表", description = """模块功能权限表 ;;uk:[[role_id module_api function_api]]""")
data class ModuleFunctionPerm(

    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx", required = true)
    var roleId: String,
    @ApiModelProperty(value = "功能api ;;fk:[core_module_function cascade]", required = true)
    var functionApi: String,
    @ApiModelProperty(value = "是否有访问权限")
    var perm: Boolean,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
