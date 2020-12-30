package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectMappingItemDto
import github.afezeria.hymn.core.module.entity.BizObjectMappingItem

/**
 * @author afezeria
 */
internal interface BizObjectMappingItemService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectMappingItemDto): Int

    fun create(dto: BizObjectMappingItemDto): String

    fun findAll(): MutableList<BizObjectMappingItem>

    fun findById(id: String): BizObjectMappingItem?

    fun findByIds(ids: List<String>): MutableList<BizObjectMappingItem>


}