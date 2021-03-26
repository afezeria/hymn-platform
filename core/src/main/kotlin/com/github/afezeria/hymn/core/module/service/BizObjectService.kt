package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.BizObjectDto
import com.github.afezeria.hymn.core.module.entity.BizObject

/**
 * @author afezeria
 */
interface BizObjectService {
    fun findAllActiveObject(): MutableList<BizObject>
    fun findActiveObjectById(id: String): BizObject?
    fun findActiveObjectByIds(ids: Collection<String>): MutableList<BizObject>
    fun findActiveObjectByApi(
        api: String,
    ): BizObject?

    fun findActiveObjectByApiList(
        apiList: Collection<String>,
    ): List<BizObject>

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObject>
    fun findAllInactiveObject(): List<BizObject>
    fun removeById(id: String): Int
    fun update(id: String, dto: BizObjectDto): Int
    fun create(dto: BizObjectDto): String
    fun inactivateById(id: String): Int
    fun activateById(id: String): Int
}