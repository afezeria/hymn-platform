package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 模块功能表，模块中的功能需要根据角色进行权限控制时在该表中添加相关数据 ;;uk:[[module_id api]]
 * @author afezeria
 */
@ApiModel(value="模块功能表，模块中的功能需要根据角色进行权限控制时在该表中添加相关数据",description = """模块功能表，模块中的功能需要根据角色进行权限控制时在该表中添加相关数据 ;;uk:[[module_id api]]""")
data class ModuleFunction(

    @ApiModelProperty(value = "关联模块 ;;fk:[core_module cascade];idx")
    var moduleId: String,
    @ApiModelProperty(value = "功能api名称，格式为模块名+功能名，例：wechat.approval ;;uk")
    var api: String,
    @ApiModelProperty(value = "功能名称")
    var name: String,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
