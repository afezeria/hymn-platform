package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BusinessCodeRefDao
import com.github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import com.github.afezeria.hymn.core.module.entity.BusinessCodeRef
import com.github.afezeria.hymn.core.module.service.BusinessCodeRefService
import com.github.afezeria.hymn.core.module.view.BusinessCodeRefListView
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.dsl.isNotNull
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

    override fun removeAutoGenData(
        byTriggerId: String?,
        byApiId: String?,
        byFunctionId: String?
    ): Int {
        var count = 0
        if (byTriggerId != null) {
            count += businessCodeRefDao.delete { (it.byTriggerId eq byTriggerId) and (it.autoGen eq true) }
        }
        if (byApiId != null) {
            count += businessCodeRefDao.delete { (it.byApiId eq byApiId) and (it.autoGen eq true) }
        }
        if (byFunctionId != null) {
            count += businessCodeRefDao.delete { (it.byFunctionId eq byFunctionId) and (it.autoGen eq true) }
        }
        return count
    }

    override fun save(dtoList: Collection<BusinessCodeRefDto>): Int {
        return businessCodeRefDao.save(dtoList.map { it.toEntity() })
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

    override fun findByTriggerIdsAndRefFunctionIdNotNull(triggerIds: List<String>): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.select({ (it.byTriggerId inList triggerIds) and it.refFunctionId.isNotNull() })
    }

    override fun findByApiId(apiId: String): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.select({ it.byApiId eq apiId })
    }

    override fun findByFunctionId(apiId: String): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.select({ it.byFunctionId eq apiId })
    }

    override fun pageFindView(
        byObjectId: String?,
        byTriggerId: String?,
        byApiId: String?,
        byFunctionId: String?,
        refObjectId: String?,
        refFieldId: String?,
        refFunctionId: String?,
        pageSize: Int,
        pageNum: Int
    ): List<BusinessCodeRefListView> {
        if (pageSize < 1) throw IllegalArgumentException("pageSize must be greater than 0, current value $pageSize")
        if (pageNum < 1) throw IllegalArgumentException("pageNum must be greater than 0, current value $pageNum")
        return businessCodeRefDao.pageSelectView(
            byObjectId,
            byTriggerId,
            byApiId,
            byFunctionId,
            refObjectId,
            refFieldId,
            refFunctionId,
            (pageNum - 1) * pageSize,
            pageSize,
        )
    }


}