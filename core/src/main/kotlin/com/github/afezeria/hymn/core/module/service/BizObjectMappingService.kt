package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectMappingDto
import com.github.afezeria.hymn.core.module.entity.BizObjectMapping
import com.github.afezeria.hymn.core.module.view.BizObjectMappingListView

/**
 * @author afezeria
 */
interface BizObjectMappingService {
    fun removeById(id: String): Int
    fun create(dto: BizObjectMappingDto): String
    fun findAll(): MutableList<BizObjectMapping>
    fun findById(id: String): BizObjectMapping?
    fun findBySourceBizObjectId(
        sourceBizObjectId: String,
    ): MutableList<BizObjectMapping>

    fun pageFind(
        sourceBizObjectId: String? = null,
        targetBizObjectId: String? = null,
        pageSize: Int,
        pageNum: Int
    ): List<BizObjectMapping>

    fun findViewById(id: String): BizObjectMappingListView?
    fun pageFindView(
        sourceBizObjectId: String?,
        targetBizObjectId: String?,
        pageSize: Int,
        pageNum: Int
    ): MutableList<BizObjectMappingListView>
}