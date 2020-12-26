package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectType
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypeDto(
    @ApiModelProperty(value = "所属业务对象id ;;fk:[core_biz_object cascade]")
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型名称")
    var name: String,
    @ApiModelProperty(value = "是否启用")
    var active: Boolean,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
) {
    fun toEntity(): BizObjectType {
        return BizObjectType(
            bizObjectId = bizObjectId,
            name = name,
            active = active,
            remark = remark,
        )
    }

    fun update(entity: BizObjectType) {
        entity.also {
            it.bizObjectId = bizObjectId
            it.name = name
            it.active = active
            it.remark = remark
        }
    }
}
