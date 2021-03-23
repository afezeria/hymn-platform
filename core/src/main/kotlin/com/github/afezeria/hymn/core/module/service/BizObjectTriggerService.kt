package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectTriggerDto
import com.github.afezeria.hymn.core.module.entity.BizObjectTrigger

/**
 * @author afezeria
 */
interface BizObjectTriggerService {
    fun removeById(id: String): Int
    fun update(id: String, dto: BizObjectTriggerDto): Int
    fun create(dto: BizObjectTriggerDto): String
    fun findByBizObjectId(bizObjectId: String): MutableList<BizObjectTrigger>
    fun findById(id: String): BizObjectTrigger?
}