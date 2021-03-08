package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BusinessCodeRefDao
import github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import github.afezeria.hymn.core.module.view.BusinessCodeRefListView
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

    fun pageFindView(
        byTriggerId: String?,
        byInterfaceId: String?,
        byCustomFunctionId: String?,
        bizObjectId: String?,
        fieldId: String?,
        customFunctionId: String?,
        pageSize: Int,
        pageNum: Int
    ): List<BusinessCodeRefListView> {
        if (pageSize < 1) throw IllegalArgumentException("pageSize must be greater than 0, current value $pageSize")
        if (pageNum < 1) throw IllegalArgumentException("pageNum must be greater than 0, current value $pageNum")
        return businessCodeRefDao.pageSelectView(
            byTriggerId,
            byInterfaceId,
            byCustomFunctionId,
            bizObjectId,
            fieldId,
            customFunctionId,
            (pageNum - 1) * pageSize,
            pageSize,
        )
    }


}