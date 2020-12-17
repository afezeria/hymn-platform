package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import github.afezeria.hymn.core.module.dto.BizObjectMappingItemDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface BizObjectMappingItemService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectMappingItemDto): Int

    fun create(dto: BizObjectMappingItemDto): String

    fun findAll(): MutableList<BizObjectMappingItem>

    fun findById(id: String): BizObjectMappingItem?

    fun findByIds(ids: List<String>): MutableList<BizObjectMappingItem>


}