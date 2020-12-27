package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
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

    fun findAll(): MutableList<ModuleFunctionPerm>

    fun findById(id: String): ModuleFunctionPerm?

    fun findByIds(ids: List<String>): MutableList<ModuleFunctionPerm>

    fun findByRoleIdAndModuleApiAndFunctionApi(
        roleId: String,
        moduleApi: String,
        functionApi: String,
    ): ModuleFunctionPerm?

    fun findByRoleId(
        roleId: String,
    ): MutableList<ModuleFunctionPerm>


    fun batchCreate(dtoList: List<ModuleFunctionPermDto>): MutableList<Int>

    /**
     * insert or update on conflict (roleId,functionApi)
     */
    fun batchSave(dtoList: List<ModuleFunctionPermDto>): MutableList<Int>


}