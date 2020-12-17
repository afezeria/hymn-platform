package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.CustomComponent
import github.afezeria.hymn.core.module.dto.CustomComponentDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface CustomComponentService {

    fun removeById(id: String): Int

    fun update(id: String, dto: CustomComponentDto): Int

    fun create(dto: CustomComponentDto): String

    fun findAll(): MutableList<CustomComponent>

    fun findById(id: String): CustomComponent?

    fun findByIds(ids: List<String>): MutableList<CustomComponent>

    fun findByApi(
        api: String,
    ): CustomComponent?


}