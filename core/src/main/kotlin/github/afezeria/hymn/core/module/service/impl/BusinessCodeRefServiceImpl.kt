package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BusinessCodeRefDao
import github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import github.afezeria.hymn.core.module.service.BusinessCodeRefService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BusinessCodeRefServiceImpl : BusinessCodeRefService {

    @Autowired
    private lateinit var businessCodeRefDao: BusinessCodeRefDao

    @Autowired
    private lateinit var dbService: DatabaseService


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

    override fun findAll(): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectAll()
    }


    override fun findById(id: String): BusinessCodeRef? {
        return businessCodeRefDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectByIds(ids)
    }


    override fun findByFieldId(
        fieldId: String,
    ): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectByFieldId(fieldId)
    }

    override fun findByOrgId(
        orgId: String,
    ): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectByOrgId(orgId)
    }

    override fun findByRoleId(
        roleId: String,
    ): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectByRoleId(roleId)
    }


}