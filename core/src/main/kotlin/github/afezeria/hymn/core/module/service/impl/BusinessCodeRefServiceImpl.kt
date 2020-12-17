package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import github.afezeria.hymn.core.module.dao.BusinessCodeRefDao
import github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import github.afezeria.hymn.core.module.service.BusinessCodeRefService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class BusinessCodeRefServiceImpl : BusinessCodeRefService {

    @Autowired
    private lateinit var businessCodeRefDao: BusinessCodeRefDao


    override fun removeById(id: String): Int {
        businessCodeRefDao.selectById(id)
            ?: throw DataNotFoundException("BusinessCodeRef".msgById(id))
        val i = businessCodeRefDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BusinessCodeRefDto): Int {
        val e = businessCodeRefDao.selectById(id)
            ?: throw DataNotFoundException("BusinessCodeRef".msgById(id))
        dto.update(e)
        val i = businessCodeRefDao.update(e)
        return i
    }

    override fun create(dto: BusinessCodeRefDto): String {
        val e = dto.toEntity()
        val id = businessCodeRefDao.insert(e)
        return id
    }

    override fun findAll(): List<BusinessCodeRef> {
        return businessCodeRefDao.selectAll()
    }


    override fun findById(id: String): BusinessCodeRef? {
        return businessCodeRefDao.selectById(id)
    }

    override fun findByFieldId(
        fieldId: String,
    ): List<BusinessCodeRef> {
        return businessCodeRefDao.selectByFieldId(fieldId,)
    }

    override fun findByOrgId(
        orgId: String,
    ): List<BusinessCodeRef> {
        return businessCodeRefDao.selectByOrgId(orgId,)
    }

    override fun findByRoleId(
        roleId: String,
    ): List<BusinessCodeRef> {
        return businessCodeRefDao.selectByRoleId(roleId,)
    }


}