package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class BizObjectFieldPermDto(
    @ApiModelProperty(value = "角色id ;;fk:[core_role cascade];idx")
    var roleId: String,
    @ApiModelProperty(value = "对象id ;;fk:[core_biz_object cascade]")
    var bizObjectId: String,
    @ApiModelProperty(value = "字段id ;;fk:[core_biz_object_field cascade];idx")
    var fieldId: String,
    @ApiModelProperty(value = "可读")
    var pRead: Boolean,
    @ApiModelProperty(value = "可编辑")
    var pEdit: Boolean,
){
    fun toEntity(): BizObjectFieldPerm {
        return BizObjectFieldPerm(
            roleId = roleId,
            bizObjectId = bizObjectId,
            fieldId = fieldId,
            pRead = pRead,
            pEdit = pEdit,
        )
    }

    fun fromEntity(entity: BizObjectFieldPerm): BizObjectFieldPermDto {
        return entity.run {
            BizObjectFieldPermDto(
                roleId = roleId,
                bizObjectId = bizObjectId,
                fieldId = fieldId,
                pRead = pRead,
                pEdit = pEdit,
          )
        }
    }

    fun update(entity: BizObjectFieldPerm) {
        entity.also {
            it.roleId = roleId
            it.bizObjectId = bizObjectId
            it.fieldId = fieldId
            it.pRead = pRead
            it.pEdit = pEdit
        }
    }
}
