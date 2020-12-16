package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.CronJob
import github.afezeria.hymn.core.module.dao.CronJobDao
import github.afezeria.hymn.core.module.dto.CronJobDto
import github.afezeria.hymn.core.module.service.CronJobService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class CronJobServiceImpl(
    private val cronJobDao: CronJobDao,
) : CronJobService {
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

    override fun findAll(): List<CronJob> {
        return cronJobDao.selectAll()
    }


    override fun findById(id: String): CronJob? {
        return cronJobDao.selectById(id)
    }

    override fun findBySharedCodeId(
        sharedCodeId: String,
    ): List<CronJob> {
        return cronJobDao.selectBySharedCodeId(sharedCodeId,)
    }


}