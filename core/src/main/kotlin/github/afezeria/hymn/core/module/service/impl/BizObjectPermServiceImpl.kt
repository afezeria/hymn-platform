package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectPerm
import github.afezeria.hymn.core.module.dao.BizObjectPermDao
import github.afezeria.hymn.core.module.dto.BizObjectPermDto
import github.afezeria.hymn.core.module.service.BizObjectPermService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class BizObjectPermServiceImpl : BizObjectPermService {

    @Autowired
    lateinit var bizObjectPermDao: BizObjectPermDao


    override fun removeById(id: String): Int {
        bizObjectPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectPerm".msgById(id))
        val i = bizObjectPermDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectPermDto): Int {
        val e = bizObjectPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectPerm".msgById(id))
        dto.update(e)
        val i = bizObjectPermDao.update(e)
        return i
    }

    override fun create(dto: BizObjectPermDto): String {
        val e = dto.toEntity()
        val id = bizObjectPermDao.insert(e)
        return id
    }

    override fun findAll(): List<BizObjectPerm> {
        return bizObjectPermDao.selectAll()
    }


    override fun findById(id: String): BizObjectPerm? {
        return bizObjectPermDao.selectById(id)
    }

    override fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm? {
        return bizObjectPermDao.selectByRoleIdAndBizObjectId(roleId,bizObjectId,)
    }

    override fun findByRoleId(
        roleId: String,
    ): List<BizObjectPerm> {
        return bizObjectPermDao.selectByRoleId(roleId,)
    }

    override fun findByBizObjectId(
        bizObjectId: String,
    ): List<BizObjectPerm> {
        return bizObjectPermDao.selectByBizObjectId(bizObjectId,)
    }


}