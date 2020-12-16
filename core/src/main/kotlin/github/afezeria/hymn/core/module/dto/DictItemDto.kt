package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.DictItem
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class DictItemDto(
    @ApiModelProperty(value = "所属字典id ;;fk:[core_dict cascade];idx")
    var dictId: String,
    @ApiModelProperty(value = "字典项名称")
    var name: String,
    @ApiModelProperty(value = "字典项编码")
    var code: String,
    @ApiModelProperty(value = "父字典中的字典项编码，用于表示多个选项列表的级联关系", required = true)
    var parentCode: String? = null,
){
    fun toEntity(): DictItem {
        return DictItem(
            dictId = dictId,
            name = name,
            code = code,
            parentCode = parentCode,
        )
    }

    fun fromEntity(entity: DictItem): DictItemDto {
        return entity.run {
            DictItemDto(
                dictId = dictId,
                name = name,
                code = code,
                parentCode = parentCode,
          )
        }
    }

    fun update(entity: DictItem) {
        entity.also {
            it.dictId = dictId
            it.name = name
            it.code = code
            it.parentCode = parentCode
        }
    }
}
