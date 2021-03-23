package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.CustomMenuGroupDto
import com.github.afezeria.hymn.core.module.entity.CustomMenuGroup

/**
 * @author afezeria
 */
interface CustomMenuGroupService {
    fun removeById(id: String): Int
    fun update(id: String, dto: CustomMenuGroupDto): Int
    fun create(dto: CustomMenuGroupDto): String
    fun findAll(): MutableList<CustomMenuGroup>
    fun findById(id: String): CustomMenuGroup?
    fun findByIds(ids: List<String>): MutableList<CustomMenuGroup>
}