package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectTypeOptions
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class BizObjectTypeOptionsDto(
    @ApiModelProperty(value = "所属对象 ;;idx")
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型id ;;fk:[core_biz_object_type cascade];idx")
    var typeId: String,
    @ApiModelProperty(value = "字段id ;;fk:[core_biz_object_field cascade]")
    var fieldId: String,
    @ApiModelProperty(value = "字段关联的字典项id ;;fk:[core_dict_item cascade]")
    var dictItemId: String,
){
    fun toEntity(): BizObjectTypeOptions {
        return BizObjectTypeOptions(
            bizObjectId = bizObjectId,
            typeId = typeId,
            fieldId = fieldId,
            dictItemId = dictItemId,
        )
    }

    fun update(entity: BizObjectTypeOptions) {
        entity.also {
            it.bizObjectId = bizObjectId
            it.typeId = typeId
            it.fieldId = fieldId
            it.dictItemId = dictItemId
        }
    }
}
