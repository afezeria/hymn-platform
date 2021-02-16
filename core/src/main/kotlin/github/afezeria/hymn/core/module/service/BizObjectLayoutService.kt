package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import github.afezeria.hymn.core.module.entity.BizObjectLayout

/**
 * @author afezeria
 */
internal interface BizObjectLayoutService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectLayoutDto): Int

    fun create(dto: BizObjectLayoutDto): String

    fun findAll(): MutableList<BizObjectLayout>

    fun findById(id: String): BizObjectLayout?

    fun findByIds(ids: List<String>): MutableList<BizObjectLayout>

    fun findByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectLayout?

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectLayout>


}