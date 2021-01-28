package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectMappingDao
import github.afezeria.hymn.core.module.dto.BizObjectMappingDto
import github.afezeria.hymn.core.module.entity.BizObjectMapping
import github.afezeria.hymn.core.module.service.BizObjectMappingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectMappingServiceImpl : BizObjectMappingService {

    @Autowired
    private lateinit var bizObjectMappingDao: BizObjectMappingDao

    @Autowired
    private lateinit var dbService: DatabaseService


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

    override fun findAll(): MutableList<BizObjectMapping> {
        return bizObjectMappingDao.selectAll()
    }


    override fun findById(id: String): BizObjectMapping? {
        return bizObjectMappingDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObjectMapping> {
        return bizObjectMappingDao.selectByIds(ids)
    }


    override fun findBySourceBizObjectId(
        sourceBizObjectId: String,
    ): MutableList<BizObjectMapping> {
        return bizObjectMappingDao.selectBySourceBizObjectId(sourceBizObjectId)
    }


}