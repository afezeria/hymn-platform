package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.CustomMenuItem
import github.afezeria.hymn.core.module.dto.CustomMenuItemDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface CustomMenuItemService {

    fun removeById(id: String): Int

    fun update(id: String, dto: CustomMenuItemDto): Int

    fun create(dto: CustomMenuItemDto): String

    fun findAll(): MutableList<CustomMenuItem>

    fun findById(id: String): CustomMenuItem?

    fun findByIds(ids: List<String>): MutableList<CustomMenuItem>


}