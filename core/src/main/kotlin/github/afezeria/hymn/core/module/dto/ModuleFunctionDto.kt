package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.ModuleFunction
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class ModuleFunctionDto(
    @ApiModelProperty(value = "关联模块 ;;fk:[core_module cascade];idx")
    var moduleId: String,
    @ApiModelProperty(value = "功能api名称，格式为模块名+功能名，例：wechat.approval ;;uk")
    var api: String,
    @ApiModelProperty(value = "功能名称")
    var name: String,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
){
    fun toEntity(): ModuleFunction {
        return ModuleFunction(
            moduleId = moduleId,
            api = api,
            name = name,
            remark = remark,
        )
    }

    fun fromEntity(entity: ModuleFunction): ModuleFunctionDto {
        return entity.run {
            ModuleFunctionDto(
                moduleId = moduleId,
                api = api,
                name = name,
                remark = remark,
          )
        }
    }

    fun update(entity: ModuleFunction) {
        entity.also {
            it.moduleId = moduleId
            it.api = api
            it.name = name
            it.remark = remark
        }
    }
}
