package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import com.github.afezeria.hymn.core.module.entity.BizObjectType

/**
 * @author afezeria
 */
interface BizObjectTypeService {
    fun removeById(id: String): Int
    fun update(id: String, dto: BizObjectTypeDto): Int
    fun create(dto: BizObjectTypeDto): String
    fun findAvailableById(id: String): BizObjectType?
    fun findAvailableByIds(ids: Collection<String>): MutableList<BizObjectType>
    fun findAvailableTypeByBizObjectId(bizObjectId: String): List<BizObjectType>
}