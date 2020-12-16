package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectTypeOptions
import github.afezeria.hymn.core.module.dao.BizObjectTypeOptionsDao
import github.afezeria.hymn.core.module.dto.BizObjectTypeOptionsDto
import github.afezeria.hymn.core.module.service.BizObjectTypeOptionsService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTypeOptionsServiceImpl(
    private val bizObjectTypeOptionsDao: BizObjectTypeOptionsDao,
) : BizObjectTypeOptionsService {
    override fun removeById(id: String): Int {
        bizObjectTypeOptionsDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypeOptions".msgById(id))
        val i = bizObjectTypeOptionsDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectTypeOptionsDto): Int {
        val e = bizObjectTypeOptionsDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypeOptions".msgById(id))
        dto.update(e)
        val i = bizObjectTypeOptionsDao.update(e)
        return i
    }

    override fun create(dto: BizObjectTypeOptionsDto): String {
        val e = dto.toEntity()
        val id = bizObjectTypeOptionsDao.insert(e)
        return id
    }

    override fun findAll(): List<BizObjectTypeOptions> {
        return bizObjectTypeOptionsDao.selectAll()
    }


    override fun findById(id: String): BizObjectTypeOptions? {
        return bizObjectTypeOptionsDao.selectById(id)
    }

    override fun findByBizObjectId(
        bizObjectId: String,
    ): List<BizObjectTypeOptions> {
        return bizObjectTypeOptionsDao.selectByBizObjectId(bizObjectId,)
    }

    override fun findByTypeId(
        typeId: String,
    ): List<BizObjectTypeOptions> {
        return bizObjectTypeOptionsDao.selectByTypeId(typeId,)
    }


}