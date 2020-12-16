package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.Module
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class ModuleDto(
    @ApiModelProperty(value = "模块api ;;uk", required = true)
    var api: String? = null,
    @ApiModelProperty(value = "模块名称")
    var name: String,
    @ApiModelProperty(value = "")
    var remark: String,
    @ApiModelProperty(value = "")
    var version: String,
){
    fun toEntity(): Module {
        return Module(
            api = api,
            name = name,
            remark = remark,
            version = version,
        )
    }

    fun fromEntity(entity: Module): ModuleDto {
        return entity.run {
            ModuleDto(
                api = api,
                name = name,
                remark = remark,
                version = version,
          )
        }
    }

    fun update(entity: Module) {
        entity.also {
            it.api = api
            it.name = name
            it.remark = remark
            it.version = version
        }
    }
}
