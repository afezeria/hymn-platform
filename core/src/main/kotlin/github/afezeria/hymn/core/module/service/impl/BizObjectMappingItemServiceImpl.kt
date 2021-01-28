package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectMappingItemDao
import github.afezeria.hymn.core.module.dto.BizObjectMappingItemDto
import github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import github.afezeria.hymn.core.module.service.BizObjectMappingItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectMappingItemServiceImpl : BizObjectMappingItemService {

    @Autowired
    private lateinit var bizObjectMappingItemDao: BizObjectMappingItemDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        bizObjectMappingItemDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectMappingItem".msgById(id))
        val i = bizObjectMappingItemDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectMappingItemDto): Int {
        val e = bizObjectMappingItemDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectMappingItem".msgById(id))
        dto.update(e)
        val i = bizObjectMappingItemDao.update(e)
        return i
    }

    override fun create(dto: BizObjectMappingItemDto): String {
        val e = dto.toEntity()
        val id = bizObjectMappingItemDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<BizObjectMappingItem> {
        return bizObjectMappingItemDao.selectAll()
    }


    override fun findById(id: String): BizObjectMappingItem? {
        return bizObjectMappingItemDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObjectMappingItem> {
        return bizObjectMappingItemDao.selectByIds(ids)
    }


}