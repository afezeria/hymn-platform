package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectFieldDto
import com.github.afezeria.hymn.core.module.entity.BizObjectField

/**
 * @author afezeria
 */
interface BizObjectFieldService {
    fun removeById(id: String): Int
    fun update(id: String, dto: BizObjectFieldDto): Int
    fun create(dto: BizObjectFieldDto): String
    fun findById(id: String): BizObjectField?
    fun findByIds(ids: Collection<String>): MutableList<BizObjectField>
    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectField>

    fun findReferenceFieldByRefId(refObjectId: String): MutableList<BizObjectField>
    fun activateById(id: String): Int
    fun inactivateById(id: String): Int
}