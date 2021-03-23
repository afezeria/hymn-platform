package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import com.github.afezeria.hymn.core.module.entity.BizObjectLayout
import com.github.afezeria.hymn.core.module.view.BizObjectLayoutListView

/**
 * @author afezeria
 */
interface BizObjectLayoutService {
    fun removeById(id: String): Int
    fun update(id: String, dto: BizObjectLayoutDto): Int
    fun create(dto: BizObjectLayoutDto): String
    fun findById(id: String): BizObjectLayout?
    fun findByBizObjectId(bizObjectId: String): MutableList<BizObjectLayout>
    fun findListViewByBizObjectId(bizObjectId: String): MutableList<BizObjectLayoutListView>
}