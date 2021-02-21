package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectTriggerDto
import github.afezeria.hymn.core.module.entity.BizObjectTrigger

/**
 * @author afezeria
 */
interface BizObjectTriggerService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectTriggerDto): Int

    fun create(dto: BizObjectTriggerDto): String

    fun findAll(): MutableList<BizObjectTrigger>

    fun findById(id: String): BizObjectTrigger?

    fun findByIds(ids: List<String>): MutableList<BizObjectTrigger>

    fun findByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectTrigger?

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectTrigger>


}