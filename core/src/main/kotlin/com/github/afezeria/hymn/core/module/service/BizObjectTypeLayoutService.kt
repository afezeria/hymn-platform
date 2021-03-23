package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectTypeLayoutDto
import com.github.afezeria.hymn.core.module.view.BizObjectTypeLayoutListView

/**
 * @author afezeria
 */
interface BizObjectTypeLayoutService {
    fun findLayoutIdByRoleIdAndTypeId(roleId: String, typeId: String): String
    fun findViewByBizObjectId(bizObjectId: String): MutableList<BizObjectTypeLayoutListView>
    fun save(dtoList: List<BizObjectTypeLayoutDto>): Int
}