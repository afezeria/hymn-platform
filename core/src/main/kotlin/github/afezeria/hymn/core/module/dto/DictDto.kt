package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.Dict
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class DictDto(
    @ApiModelProperty(value = "表明当前字典是指定字段的字典，不能通用，通用字典可以被任意多选字段使用;idx")
    var fieldId: String? = null,
    @ApiModelProperty(value = "表明当前字典值依赖与其他字典")
    var parentDictId: String? = null,
    @ApiModelProperty(value = "字典名称", required = true)
    var name: String,
    @ApiModelProperty(value = "api名称", required = true)
    var api: String,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) {
    fun toEntity(): Dict {
        return Dict(
            fieldId = fieldId,
            parentDictId = parentDictId,
            name = name,
            api = api,
            remark = remark,
        )
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
