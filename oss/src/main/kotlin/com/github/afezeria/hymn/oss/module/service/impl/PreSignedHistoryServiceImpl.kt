package com.github.afezeria.hymn.oss.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.oss.module.dao.PreSignedHistoryDao
import com.github.afezeria.hymn.oss.module.dto.PreSignedHistoryDto
import com.github.afezeria.hymn.oss.module.entity.PreSignedHistory
import com.github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@Service
class PreSignedHistoryServiceImpl : PreSignedHistoryService {

    @Autowired
    private lateinit var preSignedHistoryDao: PreSignedHistoryDao

    @Autowired
    private lateinit var databaseService: DatabaseService


    override fun removeById(id: String): Int {
        preSignedHistoryDao.selectById(id)
            ?: throw DataNotFoundException("PreSignedHistory".msgById(id))
        val i = preSignedHistoryDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: PreSignedHistoryDto): Int {
        val e = preSignedHistoryDao.selectById(id)
            ?: throw DataNotFoundException("PreSignedHistory".msgById(id))
        dto.update(e)
        val i = preSignedHistoryDao.update(e)
        return i
    }

    override fun create(dto: PreSignedHistoryDto): String {
        val e = dto.toEntity()
        val id = preSignedHistoryDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<PreSignedHistory> {
        return preSignedHistoryDao.selectAll()
    }


    override fun findById(id: String): PreSignedHistory? {
        return preSignedHistoryDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<PreSignedHistory> {
        return preSignedHistoryDao.selectByIds(ids)
    }


    override fun findByFileId(
        fileId: String,
    ): MutableList<PreSignedHistory> {
        return preSignedHistoryDao.selectByFileId(fileId)
    }

    override fun pageFindBetweenCreateDate(
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        pageSize: Int,
        pageNum: Int
    ): List<PreSignedHistory> {
        return preSignedHistoryDao.pageSelectBetweenCreateDateOrderByCreateDateDesc(
            startDate,
            endDate,
            pageSize,
            pageNum
        )
    }

    override fun pageFindByFileId(
        fileId: String,
        pageSize: Int,
        pageNum: Int
    ): List<PreSignedHistory> {
        return preSignedHistoryDao.pageSelectByFileId(fileId, pageSize, pageNum)
    }

    override fun removeByIds(ids: List<String>): Int {
        return preSignedHistoryDao.deleteByIds(ids)
    }


}