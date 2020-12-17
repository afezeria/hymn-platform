package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface ModuleFunctionPermService {

    fun removeById(id: String): Int

    fun update(id: String, dto: ModuleFunctionPermDto): Int

    fun create(dto: ModuleFunctionPermDto): String

    fun findAll(): List<ModuleFunctionPerm>

    fun findById(id: String): ModuleFunctionPerm?

    fun findByRoleIdAndModuleApiAndFunctionApi(
        roleId: String,
        moduleApi: String,
        functionApi: String,
    ): ModuleFunctionPerm?

    fun findByRoleId(
        roleId: String,
    ): List<ModuleFunctionPerm>


}