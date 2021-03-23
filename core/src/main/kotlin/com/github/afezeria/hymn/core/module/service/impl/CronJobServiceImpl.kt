package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CronJobDao
import com.github.afezeria.hymn.core.module.dto.CronJobDto
import com.github.afezeria.hymn.core.module.entity.CronJob
import com.github.afezeria.hymn.core.module.service.CronJobService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CronJobServiceImpl : CronJobService {

    @Autowired
    private lateinit var cronJobDao: CronJobDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        cronJobDao.selectById(id)
            ?: throw DataNotFoundException("CronJob".msgById(id))
        val i = cronJobDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CronJobDto): Int {
        val e = cronJobDao.selectById(id)
            ?: throw DataNotFoundException("CronJob".msgById(id))
        dto.update(e)
        val i = cronJobDao.update(e)
        return i
    }

    override fun create(dto: CronJobDto): String {
        val e = dto.toEntity()
        val id = cronJobDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<CronJob> {
        return cronJobDao.selectAll()
    }


    override fun findById(id: String): CronJob? {
        return cronJobDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<CronJob> {
        return cronJobDao.selectByIds(ids)
    }


    override fun findBySharedCodeId(
        sharedCodeId: String,
    ): MutableList<CronJob> {
        return cronJobDao.selectBySharedCodeId(sharedCodeId)
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<CronJob> {
        return cronJobDao.pageSelect(null, pageSize, pageNum)
    }


}