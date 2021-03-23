package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.CustomMenuItemDto
import com.github.afezeria.hymn.core.module.entity.CustomMenuItem

/**
 * @author afezeria
 */
interface CustomMenuItemService {
    fun removeById(id: String): Int
    fun update(id: String, dto: CustomMenuItemDto): Int
    fun create(dto: CustomMenuItemDto): String
    fun findAll(): MutableList<CustomMenuItem>
    fun findById(id: String): CustomMenuItem?
    fun findByIds(ids: List<String>): MutableList<CustomMenuItem>
    fun pageFind(pageSize: Int, pageNum: Int): List<CustomMenuItem>
}