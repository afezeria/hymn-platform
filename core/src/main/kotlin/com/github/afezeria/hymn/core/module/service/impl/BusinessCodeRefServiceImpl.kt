package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BusinessCodeRefDao
import com.github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import com.github.afezeria.hymn.core.module.entity.BusinessCodeRef
import com.github.afezeria.hymn.core.module.service.BusinessCodeRefService
import com.github.afezeria.hymn.core.module.view.BusinessCodeRefListView
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
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

    override fun findByTriggerIds(triggerIds: List<String>): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.select({ it.byTriggerId inList triggerIds })
    }

    override fun findByApiId(apiId: String): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.select({ it.byApiId eq apiId })
    }

    override fun findByFunctionId(apiId: String): MutableList<BusinessCodeRef> {
        return businessCodeRefDao.select({ it.byFunctionId eq apiId })
    }

    override fun pageFindView(
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