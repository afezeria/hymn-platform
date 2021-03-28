package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.CustomFunctionDto
import com.github.afezeria.hymn.core.module.entity.CustomFunction

/**
 * @author afezeria
 */
interface CustomFunctionService {
    fun removeById(id: String): Int
    fun update(id: String, dto: CustomFunctionDto): Int
    fun create(dto: CustomFunctionDto): String
    fun findAll(): MutableList<CustomFunction>
    fun findById(id: String): CustomFunction?
    fun findBaseFunctionById(id: String): CustomFunction?
    fun findByIds(ids: Collection<String>): MutableList<CustomFunction>
    fun findByApi(
        api: String,
    ): CustomFunction?

    fun findByApiList(
        apiList: Collection<String>,
    ): List<CustomFunction>

    fun pageFind(
        pageSize: Int,
        pageNum: Int
    ): List<CustomFunction>
}