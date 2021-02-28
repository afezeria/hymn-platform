package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectLayoutDao
import github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import github.afezeria.hymn.core.module.entity.BizObjectLayout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectLayoutService {

    @Autowired
    private lateinit var bizObjectLayoutDao: BizObjectLayoutDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectLayoutDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectLayout".msgById(id))
        val i = bizObjectLayoutDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectLayoutDto): Int {
        val e = bizObjectLayoutDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectLayout".msgById(id))
        dto.update(e)
        val i = bizObjectLayoutDao.update(e)
        return i
    }

    fun create(dto: BizObjectLayoutDto): String {
        val e = dto.toEntity()
        val id = bizObjectLayoutDao.insert(e)
        return id
    }

    fun findAll(): MutableList<BizObjectLayout> {
        return bizObjectLayoutDao.selectAll()
    }


    fun findById(id: String): BizObjectLayout? {
        return bizObjectLayoutDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<BizObjectLayout> {
        return bizObjectLayoutDao.selectByIds(ids)
    }


    fun findByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectLayout? {
        return bizObjectLayoutDao.selectByBizObjectIdAndName(bizObjectId, name)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectLayout> {
        return bizObjectLayoutDao.pageSelect(null, pageSize, pageNum)
    }


}