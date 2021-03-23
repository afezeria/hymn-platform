package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import com.github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import com.github.afezeria.hymn.core.module.view.BizObjectFieldPermListView

/**
 * @author afezeria
 */
interface BizObjectFieldPermService {
    fun save(dtoList: List<BizObjectFieldPermDto>): Int
    fun findViewByFieldId(fieldId: String): List<BizObjectFieldPermListView>
    fun findViewByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): List<BizObjectFieldPermListView>

    fun findByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm?

}