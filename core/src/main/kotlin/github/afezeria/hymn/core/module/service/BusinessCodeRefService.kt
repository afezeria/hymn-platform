package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BusinessCodeRefDao
import github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BusinessCodeRefService {

    @Autowired
    private lateinit var businessCodeRefDao: BusinessCodeRefDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        businessCodeRefDao.selectById(id)
            ?: throw DataNotFoundException("BusinessCodeRef".msgById(id))
        val i = businessCodeRefDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BusinessCodeRefDto): Int {
        val e = businessCodeRefDao.selectById(id)
            ?: throw DataNotFoundException("BusinessCodeRef".msgById(id))
        dto.update(e)
        val i = businessCodeRefDao.update(e)
        return i
    }

    fun create(dto: BusinessCodeRefDto): String {
        val e = dto.toEntity()
        val id = businessCodeRefDao.insert(e)
        return id
    }

    fun findAll(): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectAll()
    }


    fun findById(id: String): BusinessCodeRef? {
        return businessCodeRefDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectByIds(ids)
    }


    fun findByFieldId(
        fieldId: String,
    ): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectByFieldId(fieldId)
    }

    fun findByOrgId(
        orgId: String,
    ): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectByOrgId(orgId)
    }

    fun findByRoleId(
        roleId: String,
    ): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.selectByRoleId(roleId)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BusinessCodeRef> {
        return businessCodeRefDao.pageSelect(null, pageSize, pageNum)
    }


}