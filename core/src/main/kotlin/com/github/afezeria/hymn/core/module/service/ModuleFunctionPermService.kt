package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import com.github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import com.github.afezeria.hymn.core.module.view.ModuleFunctionPermListView

/**
 * @author afezeria
 */
interface ModuleFunctionPermService {
    fun findByFunctionApi(functionApi: String): List<ModuleFunctionPermListView>
    fun findViewByRoleId(roleId: String): List<ModuleFunctionPermListView>
    fun save(dtoList: List<ModuleFunctionPermDto>): Int
    fun findByRoleIdAndFunctionApi(roleId: String, functionApi: String): ModuleFunctionPerm?
}