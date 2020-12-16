package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.Dict
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class DictDto(
    @ApiModelProperty(value = "表明当前字典是指定字段的字典，不能通用，通用字典可以被任意多选字段使用;idx", required = true)
    var fieldId: String? = null,
    @ApiModelProperty(value = "表明当前字典值依赖与其他字典", required = true)
    var parentDictId: String? = null,
    @ApiModelProperty(value = "字典名称")
    var name: String,
    @ApiModelProperty(value = "api名称 ;;uk")
    var api: String,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
){
    fun toEntity(): Dict {
        return Dict(
            fieldId = fieldId,
            parentDictId = parentDictId,
            name = name,
            api = api,
            remark = remark,
        )
    }

    fun fromEntity(entity: Dict): DictDto {
        return entity.run {
            DictDto(
                fieldId = fieldId,
                parentDictId = parentDictId,
                name = name,
                api = api,
                remark = remark,
          )
        }
    }

    fun update(entity: Dict) {
        entity.also {
            it.fieldId = fieldId
            it.parentDictId = parentDictId
            it.name = name
            it.api = api
            it.remark = remark
        }
    }
}
