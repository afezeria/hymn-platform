package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectTriggerDao
import github.afezeria.hymn.core.module.dto.BizObjectTriggerDto
import github.afezeria.hymn.core.module.entity.BizObjectTrigger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTriggerService {

    @Autowired
    private lateinit var bizObjectTriggerDao: BizObjectTriggerDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectTriggerDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTrigger".msgById(id))
        val i = bizObjectTriggerDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectTriggerDto): Int {
        val e = bizObjectTriggerDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTrigger".msgById(id))
        dto.update(e)
        val i = bizObjectTriggerDao.update(e)
        return i
    }

    fun create(dto: BizObjectTriggerDto): String {
        val e = dto.toEntity()
        val id = bizObjectTriggerDao.insert(e)
        return id
    }

    fun findAll(): MutableList<BizObjectTrigger> {
        return bizObjectTriggerDao.selectAll()
    }


    fun findById(id: String): BizObjectTrigger? {
        return bizObjectTriggerDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<BizObjectTrigger> {
        return bizObjectTriggerDao.selectByIds(ids)
    }


    fun findByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectTrigger? {
        return bizObjectTriggerDao.selectByBizObjectIdAndApi(bizObjectId, api)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectTrigger> {
        return bizObjectTriggerDao.pageSelect(null, pageSize, pageNum)
    }


}