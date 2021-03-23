package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectPermDto
import com.github.afezeria.hymn.core.module.entity.BizObjectPerm
import com.github.afezeria.hymn.core.module.view.BizObjectPermListView

/**
 * @author afezeria
 */
interface BizObjectPermService {
    fun save(dtoList: List<BizObjectPermDto>): Int
    fun findViewByBizObjectId(bizObjectId: String): List<BizObjectPermListView>
    fun findViewByRoleId(roleId: String): List<BizObjectPermListView>
    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm?
}