package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BizObjectMappingDao
import com.github.afezeria.hymn.core.module.dto.BizObjectMappingDto
import com.github.afezeria.hymn.core.module.entity.BizObjectMapping
import com.github.afezeria.hymn.core.module.table.CoreBizObjectMappings
import com.github.afezeria.hymn.core.module.view.BizObjectMappingListView
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.schema.ColumnDeclaring
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectMappingService {

    @Autowired
    private lateinit var bizObjectMappingDao: BizObjectMappingDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectMappingDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectMapping".msgById(id))
        val i = bizObjectMappingDao.deleteById(id)
        return i
    }

    fun create(dto: BizObjectMappingDto): String {
        val e = dto.toEntity()
        val id = bizObjectMappingDao.insert(e)
        return id
    }

    fun findAll(): MutableList<BizObjectMapping> {
        return bizObjectMappingDao.selectAll()
    }


    fun findById(id: String): BizObjectMapping? {
        return bizObjectMappingDao.selectById(id)
    }

    fun findBySourceBizObjectId(
        sourceBizObjectId: String,
    ): MutableList<BizObjectMapping> {
        return bizObjectMappingDao.selectBySourceBizObjectId(sourceBizObjectId)
    }

    fun pageFind(
        sourceBizObjectId: String? = null,
        targetBizObjectId: String? = null,
        pageSize: Int,
        pageNum: Int
    ): List<BizObjectMapping> {
        val expr: ((CoreBizObjectMappings) -> ColumnDeclaring<Boolean>)? =
            if (sourceBizObjectId != null) {
                if (targetBizObjectId != null) {
                    {
                        (it.sourceBizObjectId eq sourceBizObjectId) and
                            (it.targetBizObjectId eq targetBizObjectId)
                    }
                } else {
                    { it.sourceBizObjectId eq sourceBizObjectId }
                }
            } else {
                if (targetBizObjectId != null) {
                    { it.targetBizObjectId eq targetBizObjectId }
                } else {
                    null
                }
            }
        return bizObjectMappingDao.pageSelect(expr, pageSize, pageNum)
    }

    fun findViewById(id: String): BizObjectMappingListView? {
        return bizObjectMappingDao.selectDto({ it.id eq id }, 0, 1).firstOrNull()
    }

    fun pageFindView(
        sourceBizObjectId: String?,
        targetBizObjectId: String?,
        pageSize: Int,
        pageNum: Int
    ): MutableList<BizObjectMappingListView> {
        if (pageSize < 1) throw IllegalArgumentException("pageSize must be greater than 0, current value $pageSize")
        if (pageNum < 1) throw IllegalArgumentException("pageNum must be greater than 0, current value $pageNum")
        val expr: ((CoreBizObjectMappings) -> ColumnDeclaring<Boolean>)? =
            if (sourceBizObjectId != null) {
                if (targetBizObjectId != null) {
                    {
                        (it.sourceBizObjectId eq sourceBizObjectId) and
                            (it.targetBizObjectId eq targetBizObjectId)
                    }
                } else {
                    { it.sourceBizObjectId eq sourceBizObjectId }
                }
            } else {
                if (targetBizObjectId != null) {
                    { it.targetBizObjectId eq targetBizObjectId }
                } else {
                    null
                }
            }
        return bizObjectMappingDao.selectDto(expr, (pageNum - 1) * pageSize, pageSize)
    }
}