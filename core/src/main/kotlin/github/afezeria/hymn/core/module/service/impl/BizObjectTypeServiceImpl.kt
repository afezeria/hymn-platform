package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectType
import github.afezeria.hymn.core.module.dao.BizObjectTypeDao
import github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import github.afezeria.hymn.core.module.service.BizObjectTypeService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTypeServiceImpl(
    private val bizObjectTypeDao: BizObjectTypeDao,
) : BizObjectTypeService {
    override fun removeById(id: String): Int {
        bizObjectTypeDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectType".msgById(id))
        val i = bizObjectTypeDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectTypeDto): Int {
        val e = bizObjectTypeDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectType".msgById(id))
        dto.update(e)
        val i = bizObjectTypeDao.update(e)
        return i
    }

    override fun create(dto: BizObjectTypeDto): String {
        val e = dto.toEntity()
        val id = bizObjectTypeDao.insert(e)
        return id
    }

    override fun findAll(): List<BizObjectType> {
        return bizObjectTypeDao.selectAll()
    }


    override fun findById(id: String): BizObjectType? {
        return bizObjectTypeDao.selectById(id)
    }

    override fun findByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectType? {
        return bizObjectTypeDao.selectByBizObjectIdAndName(bizObjectId,name,)
    }


}