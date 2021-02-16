package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import github.afezeria.hymn.core.module.entity.BizObjectType

/**
 * @author afezeria
 */
internal interface BizObjectTypeService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectTypeDto): Int

    fun create(dto: BizObjectTypeDto): String

    fun findAll(): MutableList<BizObjectType>

    fun findById(id: String): BizObjectType?

    fun findByIds(ids: List<String>): MutableList<BizObjectType>

    fun findByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectType?

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectType>


}