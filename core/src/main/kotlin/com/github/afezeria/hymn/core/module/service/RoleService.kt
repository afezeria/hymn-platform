package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.RoleDto
import com.github.afezeria.hymn.core.module.entity.Role

/**
 * @author afezeria
 */
interface RoleService {
    fun removeById(id: String): Int
    fun update(id: String, dto: RoleDto): Int
    fun create(dto: RoleDto): String
    fun findAll(): MutableList<Role>
    fun findById(id: String): Role?
    fun findByIds(ids: Collection<String>): MutableList<Role>
    fun findIdList(ids: List<String>? = null): MutableList<String>
    fun pageFind(pageSize: Int, pageNum: Int): List<Role>
}