package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class BizObjectTypeLayoutDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "业务对象id ;;fk:[core_biz_object cascade];idx")
    var bizObjectId: String,
    @ApiModelProperty(value = "记录类型id ;;fk:[core_biz_object_type cascade]")
    var typeId: String,
    @ApiModelProperty(value = "页面布局id ;;fk:[core_biz_object_layout cascade]")
    var layoutId: String,
){
    fun toEntity(): BizObjectTypeLayout {
        return BizObjectTypeLayout(
            roleId = roleId,
            bizObjectId = bizObjectId,
            typeId = typeId,
            layoutId = layoutId,
        )
    }

    fun update(entity: BizObjectTypeLayout) {
        entity.also {
            it.roleId = roleId
            it.bizObjectId = bizObjectId
            it.typeId = typeId
            it.layoutId = layoutId
        }
    }
}
