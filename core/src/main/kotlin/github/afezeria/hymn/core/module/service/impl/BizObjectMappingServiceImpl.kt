package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectMapping
import github.afezeria.hymn.core.module.dao.BizObjectMappingDao
import github.afezeria.hymn.core.module.dto.BizObjectMappingDto
import github.afezeria.hymn.core.module.service.BizObjectMappingService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class BizObjectMappingServiceImpl : BizObjectMappingService {

    @Autowired
    private lateinit var bizObjectMappingDao: BizObjectMappingDao


    override fun removeById(id: String): Int {
        bizObjectMappingDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectMapping".msgById(id))
        val i = bizObjectMappingDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectMappingDto): Int {
        val e = bizObjectMappingDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectMapping".msgById(id))
        dto.update(e)
        val i = bizObjectMappingDao.update(e)
        return i
    }

    override fun create(dto: BizObjectMappingDto): String {
        val e = dto.toEntity()
        val id = bizObjectMappingDao.insert(e)
        return id
    }

    override fun findAll(): List<BizObjectMapping> {
        return bizObjectMappingDao.selectAll()
    }


    override fun findById(id: String): BizObjectMapping? {
        return bizObjectMappingDao.selectById(id)
    }

    override fun findBySourceBizObjectId(
        sourceBizObjectId: String,
    ): List<BizObjectMapping> {
        return bizObjectMappingDao.selectBySourceBizObjectId(sourceBizObjectId,)
    }


}