package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import com.github.afezeria.hymn.core.module.entity.BusinessCodeRef
import com.github.afezeria.hymn.core.module.view.BusinessCodeRefListView

/**
 * @author afezeria
 */
interface BusinessCodeRefService {
    fun removeById(id: String): Int
    fun create(dto: BusinessCodeRefDto): String
    fun findAll(): MutableList<BusinessCodeRef>
    fun findById(id: String): BusinessCodeRef?
    fun findByTriggerIds(triggerIds: List<String>): MutableList<BusinessCodeRef>
    fun findByApiId(apiId: String): MutableList<BusinessCodeRef>
    fun findByFunctionId(apiId: String): MutableList<BusinessCodeRef>
    fun pageFindView(
        byTriggerId: String?,
        byInterfaceId: String?,
        byCustomFunctionId: String?,
        bizObjectId: String?,
        fieldId: String?,
        customFunctionId: String?,
        pageSize: Int,
        pageNum: Int
    ): List<BusinessCodeRefListView>
}