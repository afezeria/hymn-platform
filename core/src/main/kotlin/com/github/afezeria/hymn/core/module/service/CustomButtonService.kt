package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.CustomButtonDto
import com.github.afezeria.hymn.core.module.entity.CustomButton

/**
 * @author afezeria
 */
interface CustomButtonService {
    fun removeById(id: String): Int
    fun update(id: String, dto: CustomButtonDto): Int
    fun create(dto: CustomButtonDto): String
    fun findAll(): MutableList<CustomButton>
    fun findById(id: String): CustomButton?
    fun findByIds(ids: Collection<String>): MutableList<CustomButton>
    fun findByApi(
        api: String,
    ): CustomButton?

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomButton>
}