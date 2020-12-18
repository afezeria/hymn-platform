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

    /**
     * 查询id
     * 查询[ids]中实际存在的id，[ids]为null时返回所有id，为空列表时返回空列表
     * @param ids id列表
     * @return id列表
     */
    fun findIdList(ids: List<String>? = null): MutableList<String>

}