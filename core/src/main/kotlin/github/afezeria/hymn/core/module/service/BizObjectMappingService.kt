package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectMappingDao
import github.afezeria.hymn.core.module.dto.BizObjectMappingDto
import github.afezeria.hymn.core.module.entity.BizObjectMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectMappingService {

    @Autowired
    private lateinit var bizObjectMappingDao: BizObjectMappingDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectMappingDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectMapping".msgById(id))
        val i = bizObjectMappingDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectMappingDto): Int {
        val e = bizObjectMappingDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectMapping".msgById(id))
        dto.update(e)
        val i = bizObjectMappingDao.update(e)
        return i
    }

    fun create(dto: BizObjectMappingDto): String {
        val e = dto.toEntity()
        val id = bizObjectMappingDao.insert(e)
        return id
    }

    fun findAll(): MutableList<BizObjectMapping> {
        return bizObjectMappingDao.selectAll()
    }


    fun findById(id: String): BizObjectMapping? {
        return bizObjectMappingDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<BizObjectMapping> {
        return bizObjectMappingDao.selectByIds(ids)
    }


    fun findBySourceBizObjectId(
        sourceBizObjectId: String,
    ): MutableList<BizObjectMapping> {
        return bizObjectMappingDao.selectBySourceBizObjectId(sourceBizObjectId)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectMapping> {
        return bizObjectMappingDao.pageSelect(null, pageSize, pageNum)
    }


}