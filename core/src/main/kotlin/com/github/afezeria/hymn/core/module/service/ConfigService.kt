package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.ConfigDto
import com.github.afezeria.hymn.core.module.entity.Config

/**
 * @author afezeria
 */
interface ConfigService {
    fun removeById(id: String): Int
    fun update(id: String, dto: ConfigDto): Int
    fun create(dto: ConfigDto): String
    fun findAll(): MutableList<Config>
    fun findById(id: String): Config?
    fun findByIds(ids: List<String>): MutableList<Config>
    fun findByKey(
        key: String,
    ): MutableList<Config>

    fun pageFind(pageSize: Int, pageNum: Int): List<Config>
}