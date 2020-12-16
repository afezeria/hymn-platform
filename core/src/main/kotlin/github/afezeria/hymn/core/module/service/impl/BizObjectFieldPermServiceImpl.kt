package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import github.afezeria.hymn.core.module.dao.BizObjectFieldPermDao
import github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import github.afezeria.hymn.core.module.service.BizObjectFieldPermService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectFieldPermServiceImpl(
    private val bizObjectFieldPermDao: BizObjectFieldPermDao,
) : BizObjectFieldPermService {
    override fun removeById(id: String): Int {
        bizObjectFieldPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectFieldPerm".msgById(id))
        val i = bizObjectFieldPermDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectFieldPermDto): Int {
        val e = bizObjectFieldPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectFieldPerm".msgById(id))
        dto.update(e)
        val i = bizObjectFieldPermDao.update(e)
        return i
    }

    override fun create(dto: BizObjectFieldPermDto): String {
        val e = dto.toEntity()
        val id = bizObjectFieldPermDao.insert(e)
        return id
    }

    override fun findAll(): List<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectAll()
    }


    override fun findById(id: String): BizObjectFieldPerm? {
        return bizObjectFieldPermDao.selectById(id)
    }

    override fun findByRoleId(
        roleId: String,
    ): List<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectByRoleId(roleId,)
    }

    override fun findByFieldId(
        fieldId: String,
    ): List<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectByFieldId(fieldId,)
    }

    override fun findByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm? {
        return bizObjectFieldPermDao.selectByRoleIdAndFieldId(roleId,fieldId,)
    }


}