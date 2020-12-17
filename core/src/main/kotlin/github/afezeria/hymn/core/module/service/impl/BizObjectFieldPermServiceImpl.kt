package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import github.afezeria.hymn.core.module.dao.BizObjectFieldPermDao
import github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import github.afezeria.hymn.core.module.service.BizObjectFieldPermService
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class BizObjectFieldPermServiceImpl : BizObjectFieldPermService {

    @Autowired
    private lateinit var bizObjectFieldPermDao: BizObjectFieldPermDao

    @Autowired
    private lateinit var dbService: DataBaseService


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

    override fun findAll(): MutableList<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectAll()
    }


    override fun findById(id: String): BizObjectFieldPerm? {
        return bizObjectFieldPermDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectByIds(ids)
    }


    override fun findByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm? {
        return bizObjectFieldPermDao.selectByRoleIdAndFieldId(roleId,fieldId,)
    }

    override fun findByRoleId(
        roleId: String,
    ): MutableList<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectByRoleId(roleId,)
    }

    override fun findByFieldId(
        fieldId: String,
    ): MutableList<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectByFieldId(fieldId,)
    }


}