package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.Module
import github.afezeria.hymn.core.module.dto.ModuleDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface ModuleService {

    fun removeById(id: String): Int

    fun update(id: String, dto: ModuleDto): Int

    fun create(dto: ModuleDto): String

    fun findAll(): List<Module>

    fun findById(id: String): Module?

    fun findByApi(
        api: String,
    ): Module?


}