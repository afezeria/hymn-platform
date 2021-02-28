package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.CronJobDao
import github.afezeria.hymn.core.module.dto.CronJobDto
import github.afezeria.hymn.core.module.entity.CronJob
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CronJobService {

    @Autowired
    private lateinit var cronJobDao: CronJobDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        cronJobDao.selectById(id)
            ?: throw DataNotFoundException("CronJob".msgById(id))
        val i = cronJobDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: CronJobDto): Int {
        val e = cronJobDao.selectById(id)
            ?: throw DataNotFoundException("CronJob".msgById(id))
        dto.update(e)
        val i = cronJobDao.update(e)
        return i
    }

    fun create(dto: CronJobDto): String {
        val e = dto.toEntity()
        val id = cronJobDao.insert(e)
        return id
    }

    fun findAll(): MutableList<CronJob> {
        return cronJobDao.selectAll()
    }


    fun findById(id: String): CronJob? {
        return cronJobDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<CronJob> {
        return cronJobDao.selectByIds(ids)
    }


    fun findBySharedCodeId(
        sharedCodeId: String,
    ): MutableList<CronJob> {
        return cronJobDao.selectBySharedCodeId(sharedCodeId)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<CronJob> {
        return cronJobDao.pageSelect(null, pageSize, pageNum)
    }


}