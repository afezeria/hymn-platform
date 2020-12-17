package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.RoleDto
import github.afezeria.hymn.core.module.entity.Role

/**
 * @author afezeria
 */
interface RoleService {

    fun removeById(id: String): Int

    fun update(id: String, dto: RoleDto): Int

    fun create(dto: RoleDto): String

    fun findAll(): MutableList<Role>

    fun findById(id: String): Role?

    fun findByIds(ids: List<String>): MutableList<Role>

    fun findIdList(ids:List<String> = emptyList()): MutableList<String>

}