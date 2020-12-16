package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import github.afezeria.hymn.core.module.dao.BizObjectTypePermDao
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import github.afezeria.hymn.core.module.service.BizObjectTypePermService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectTypePermServiceImpl(
    private val bizObjectTypePermDao: BizObjectTypePermDao,
) : BizObjectTypePermService {
    override fun removeById(id: String): Int {
        bizObjectTypePermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypePerm".msgById(id))
        val i = bizObjectTypePermDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectTypePermDto): Int {
        val e = bizObjectTypePermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypePerm".msgById(id))
        dto.update(e)
        val i = bizObjectTypePermDao.update(e)
        return i
    }

    override fun create(dto: BizObjectTypePermDto): String {
        val e = dto.toEntity()
        val id = bizObjectTypePermDao.insert(e)
        return id
    }

    override fun findAll(): List<BizObjectTypePerm> {
        return bizObjectTypePermDao.selectAll()
    }


    override fun findById(id: String): BizObjectTypePerm? {
        return bizObjectTypePermDao.selectById(id)
    }

    override fun findByRoleIdAndTypeId(
        roleId: String,
        typeId: String,
    ): BizObjectTypePerm? {
        return bizObjectTypePermDao.selectByRoleIdAndTypeId(roleId,typeId,)
    }

    override fun findByTypeId(
        typeId: String,
    ): List<BizObjectTypePerm> {
        return bizObjectTypePermDao.selectByTypeId(typeId,)
    }


}