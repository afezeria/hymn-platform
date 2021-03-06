package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectTypePermDto(
    @ApiModelProperty(value = "角色id", required = true)
    var roleId: String,
    @ApiModelProperty(
        value = "类型id，新建类型时使用空字符串占位",
        required = true
    )
    var typeId: String,
    @ApiModelProperty(value = "创建数据时选择特定记录类型的权限", required = true)
    var visible: Boolean = false,
) {

    fun toEntity(): BizObjectTypePerm {
        return BizObjectTypePerm(
            roleId = roleId,
            typeId = typeId,
            visible = visible,
        )
    }

    fun update(entity: BizObjectTypePerm) {
        entity.also {
            it.roleId = roleId
            it.typeId = typeId
            it.visible = visible
        }
    }
}
