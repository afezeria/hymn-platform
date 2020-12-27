package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectMappingDto
import github.afezeria.hymn.core.module.entity.BizObjectMapping

/**
 * @author afezeria
 */
interface BizObjectMappingService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectMappingDto): Int

    fun create(dto: BizObjectMappingDto): String

    fun findAll(): MutableList<BizObjectMapping>

    fun findById(id: String): BizObjectMapping?

    fun findByIds(ids: List<String>): MutableList<BizObjectMapping>

    fun findBySourceBizObjectId(
        sourceBizObjectId: String,
    ): MutableList<BizObjectMapping>


}