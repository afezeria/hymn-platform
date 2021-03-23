package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.CustomApiDto
import com.github.afezeria.hymn.core.module.entity.CustomApi

/**
 * @author afezeria
 */
interface CustomApiService {
    fun removeById(id: String): Int
    fun update(id: String, dto: CustomApiDto): Int
    fun create(dto: CustomApiDto): String
    fun findAll(): MutableList<CustomApi>
    fun findById(id: String): CustomApi?
    fun findByIds(ids: List<String>): MutableList<CustomApi>

    fun findByApi(
        api: String,
    ): CustomApi?

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomApi>
}