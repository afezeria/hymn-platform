package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectTypeFieldOptionDto
import com.github.afezeria.hymn.core.module.entity.BizObjectTypeFieldOption
import com.github.afezeria.hymn.core.module.view.BizObjectTypeFieldOptionListView

/**
 * @author afezeria
 */
interface BizObjectTypeFieldOptionService {
    fun removeById(id: String): Int
    fun update(id: String, dto: BizObjectTypeFieldOptionDto): Int
    fun create(dto: BizObjectTypeFieldOptionDto): String
    fun findAll(): MutableList<BizObjectTypeFieldOption>
    fun findById(id: String): BizObjectTypeFieldOption?
    fun findByIds(ids: List<String>): MutableList<BizObjectTypeFieldOption>
    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectTypeFieldOption>

    fun findByTypeId(
        typeId: String,
    ): MutableList<BizObjectTypeFieldOption>

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectTypeFieldOption>
    fun findView(typeId: String, fieldIds: List<String>): List<BizObjectTypeFieldOptionListView>
    fun save(dtoList: List<BizObjectTypeFieldOptionDto>): Int
}