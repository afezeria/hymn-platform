package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectType
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypeDto(
    @ApiModelProperty(value = "所属业务对象id ;;fk:[core_biz_object cascade]", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型名称", required = true)
    var name: String,
    @ApiModelProperty(value = "是否启用", required = true)
    var active: Boolean,
    @ApiModelProperty(value = "")
    var remark: String? = null,
    @ApiModelProperty(value = "类型权限")
    var permList: List<BizObjectTypePermDto> = emptyList(),
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
