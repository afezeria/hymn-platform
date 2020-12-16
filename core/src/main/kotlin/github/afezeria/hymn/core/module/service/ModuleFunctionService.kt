package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.ModuleFunction
import github.afezeria.hymn.core.module.dto.ModuleFunctionDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface ModuleFunctionService {

    fun removeById(id: String): Int

    fun update(id: String, dto: ModuleFunctionDto): Int

    fun create(dto: ModuleFunctionDto): String

    fun findAll(): List<ModuleFunction>

    fun findById(id: String): ModuleFunction?

    fun findByModuleIdAndApi(
        moduleId: String,
        api: String,
    ): ModuleFunction?

    fun findByModuleId(
        moduleId: String,
    ): List<ModuleFunction>

    fun findByApi(
        api: String,
    ): ModuleFunction?


}