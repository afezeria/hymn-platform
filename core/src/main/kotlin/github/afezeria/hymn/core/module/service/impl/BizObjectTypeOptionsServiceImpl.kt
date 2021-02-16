package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectTypeOptionsDao
import github.afezeria.hymn.core.module.dto.BizObjectTypeOptionsDto
import github.afezeria.hymn.core.module.entity.BizObjectTypeOptions
import github.afezeria.hymn.core.module.service.BizObjectTypeOptionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypeOptionsServiceImpl : BizObjectTypeOptionsService {

    @Autowired
    private lateinit var bizObjectTypeOptionsDao: BizObjectTypeOptionsDao

    @Autowired
    private lateinit var dbService: DatabaseService


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

    override fun findAll(): MutableList<BizObjectTypeOptions> {
        return bizObjectTypeOptionsDao.selectAll()
    }


    override fun findById(id: String): BizObjectTypeOptions? {
        return bizObjectTypeOptionsDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObjectTypeOptions> {
        return bizObjectTypeOptionsDao.selectByIds(ids)
    }


    override fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectTypeOptions> {
        return bizObjectTypeOptionsDao.selectByBizObjectId(bizObjectId)
    }

    override fun findByTypeId(
        typeId: String,
    ): MutableList<BizObjectTypeOptions> {
        return bizObjectTypeOptionsDao.selectByTypeId(typeId)
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectTypeOptions> {
        return bizObjectTypeOptionsDao.pageSelect(null, pageSize, pageNum)
    }


}