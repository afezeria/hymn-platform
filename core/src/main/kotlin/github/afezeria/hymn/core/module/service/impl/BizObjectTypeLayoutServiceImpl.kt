package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout
import github.afezeria.hymn.core.module.dao.BizObjectTypeLayoutDao
import github.afezeria.hymn.core.module.dto.BizObjectTypeLayoutDto
import github.afezeria.hymn.core.module.service.BizObjectTypeLayoutService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class BizObjectTypeLayoutServiceImpl : BizObjectTypeLayoutService {

    @Autowired
    lateinit var bizObjectTypeLayoutDao: BizObjectTypeLayoutDao


    override fun removeById(id: String): Int {
        bizObjectTypeLayoutDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypeLayout".msgById(id))
        val i = bizObjectTypeLayoutDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectTypeLayoutDto): Int {
        val e = bizObjectTypeLayoutDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypeLayout".msgById(id))
        dto.update(e)
        val i = bizObjectTypeLayoutDao.update(e)
        return i
    }

    override fun create(dto: BizObjectTypeLayoutDto): String {
        val e = dto.toEntity()
        val id = bizObjectTypeLayoutDao.insert(e)
        return id
    }

    override fun findAll(): List<BizObjectTypeLayout> {
        return bizObjectTypeLayoutDao.selectAll()
    }


    override fun findById(id: String): BizObjectTypeLayout? {
        return bizObjectTypeLayoutDao.selectById(id)
    }

    override fun findByRoleIdAndTypeIdAndLayoutId(
        roleId: String,
        typeId: String,
        layoutId: String,
    ): BizObjectTypeLayout? {
        return bizObjectTypeLayoutDao.selectByRoleIdAndTypeIdAndLayoutId(roleId,typeId,layoutId,)
    }

    override fun findByRoleId(
        roleId: String,
    ): List<BizObjectTypeLayout> {
        return bizObjectTypeLayoutDao.selectByRoleId(roleId,)
    }

    override fun findByBizObjectId(
        bizObjectId: String,
    ): List<BizObjectTypeLayout> {
        return bizObjectTypeLayoutDao.selectByBizObjectId(bizObjectId,)
    }


}