package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.DictItem
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class DictItemDto(
    @ApiModelProperty(value = "所属字典id", required = true)
    var dictId: String,
    @ApiModelProperty(value = "字典项名称", required = true)
    var name: String,
    @ApiModelProperty(value = "字典项编码", required = true)
    var code: String,
    @ApiModelProperty(value = "父字典中的字典项编码，用于表示多个选项列表的级联关系")
    var parentCode: String? = null,
) {
    fun toEntity(): DictItem {
        return DictItem(
            dictId = dictId,
            name = name,
            code = code,
            parentCode = parentCode,
        )
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
