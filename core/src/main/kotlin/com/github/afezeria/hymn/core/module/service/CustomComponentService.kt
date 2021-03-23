package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.CustomComponentDto
import com.github.afezeria.hymn.core.module.entity.CustomComponent

/**
 * @author afezeria
 */
interface CustomComponentService {
    fun removeById(id: String): Int
    fun update(id: String, dto: CustomComponentDto): Int
    fun create(dto: CustomComponentDto): String
    fun findAll(): MutableList<CustomComponent>
    fun findById(id: String): CustomComponent?
    fun findByIds(ids: List<String>): MutableList<CustomComponent>
    fun findByApi(
        api: String,
    ): CustomComponent?

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomComponent>
}