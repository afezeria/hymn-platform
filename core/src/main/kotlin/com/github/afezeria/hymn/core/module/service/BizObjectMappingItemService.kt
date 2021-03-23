package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectMappingItemDto
import com.github.afezeria.hymn.core.module.entity.BizObjectMappingItem

/**
 * @author afezeria
 */
interface BizObjectMappingItemService {
    fun removeById(id: String): Int
    fun save(dtoList: List<BizObjectMappingItemDto>): Int
    fun create(dto: BizObjectMappingItemDto): String
    fun findByMappingId(mappingId: String): MutableList<BizObjectMappingItem>
}