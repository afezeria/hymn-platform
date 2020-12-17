package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectLayout
import github.afezeria.hymn.core.module.dao.BizObjectLayoutDao
import github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import github.afezeria.hymn.core.module.service.BizObjectLayoutService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class BizObjectLayoutServiceImpl : BizObjectLayoutService {

    @Autowired
    lateinit var bizObjectLayoutDao: BizObjectLayoutDao


    override fun removeById(id: String): Int {
        bizObjectLayoutDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectLayout".msgById(id))
        val i = bizObjectLayoutDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectLayoutDto): Int {
        val e = bizObjectLayoutDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectLayout".msgById(id))
        dto.update(e)
        val i = bizObjectLayoutDao.update(e)
        return i
    }

    override fun create(dto: BizObjectLayoutDto): String {
        val e = dto.toEntity()
        val id = bizObjectLayoutDao.insert(e)
        return id
    }

    override fun findAll(): List<BizObjectLayout> {
        return bizObjectLayoutDao.selectAll()
    }


    override fun findById(id: String): BizObjectLayout? {
        return bizObjectLayoutDao.selectById(id)
    }

    override fun findByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectLayout? {
        return bizObjectLayoutDao.selectByBizObjectIdAndName(bizObjectId,name,)
    }


}