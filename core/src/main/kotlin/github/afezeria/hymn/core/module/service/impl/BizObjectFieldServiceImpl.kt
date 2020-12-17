package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectField
import github.afezeria.hymn.core.module.dao.BizObjectFieldDao
import github.afezeria.hymn.core.module.dto.BizObjectFieldDto
import github.afezeria.hymn.core.module.service.BizObjectFieldService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class BizObjectFieldServiceImpl : BizObjectFieldService {

    @Autowired
    private lateinit var bizObjectFieldDao: BizObjectFieldDao


    override fun removeById(id: String): Int {
        bizObjectFieldDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectField".msgById(id))
        val i = bizObjectFieldDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectFieldDto): Int {
        val e = bizObjectFieldDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectField".msgById(id))
        dto.update(e)
        val i = bizObjectFieldDao.update(e)
        return i
    }

    override fun create(dto: BizObjectFieldDto): String {
        val e = dto.toEntity()
        val id = bizObjectFieldDao.insert(e)
        return id
    }

    override fun findAll(): List<BizObjectField> {
        return bizObjectFieldDao.selectAll()
    }


    override fun findById(id: String): BizObjectField? {
        return bizObjectFieldDao.selectById(id)
    }

    override fun findByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectField? {
        return bizObjectFieldDao.selectByBizObjectIdAndApi(bizObjectId,api,)
    }

    override fun findByBizObjectId(
        bizObjectId: String,
    ): List<BizObjectField> {
        return bizObjectFieldDao.selectByBizObjectId(bizObjectId,)
    }


}