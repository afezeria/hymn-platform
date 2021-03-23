package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.DictDto
import com.github.afezeria.hymn.core.module.entity.Dict

/**
 * @author afezeria
 */
interface DictService {
    fun removeById(id: String): Int
    fun update(id: String, dto: DictDto): Int
    fun create(dto: DictDto): String
    fun findAll(): MutableList<Dict>
    fun findById(id: String): Dict?
    fun findByIds(ids: List<String>): MutableList<Dict>
    fun findByApi(
        api: String,
    ): Dict?

    fun pageFind(pageSize: Int, pageNum: Int): List<Dict>
}