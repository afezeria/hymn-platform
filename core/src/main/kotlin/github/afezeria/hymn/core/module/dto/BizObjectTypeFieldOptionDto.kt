package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectTypeFieldOption
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypeFieldOptionDto(
    @ApiModelProperty(value = "所属对象", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型id", required = true)
    var typeId: String,
    @ApiModelProperty(value = "字段id", required = true)
    var fieldId: String,
    @ApiModelProperty(value = "字段关联的字典项id", required = true)
    var dictItemId: String,
) {
    fun toEntity(): BizObjectTypeFieldOption {
        return BizObjectTypeFieldOption(
            bizObjectId = bizObjectId,
            typeId = typeId,
            fieldId = fieldId,
            dictItemId = dictItemId,
        )
    }

    fun update(entity: BizObjectTypeFieldOption) {
        entity.also {
            it.bizObjectId = bizObjectId
            it.typeId = typeId
            it.fieldId = fieldId
            it.dictItemId = dictItemId
        }
    }
}
