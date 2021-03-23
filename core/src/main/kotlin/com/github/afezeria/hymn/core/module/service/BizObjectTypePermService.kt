package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import com.github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import com.github.afezeria.hymn.core.module.view.BizObjectTypePermListView

/**
 * @author afezeria
 */
interface BizObjectTypePermService {
    fun save(dtoList: List<BizObjectTypePermDto>): Int
    fun findViewByTypeId(typeId: String): List<BizObjectTypePermListView>
    fun findViewByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): List<BizObjectTypePermListView>

    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): MutableList<BizObjectTypePerm>

}