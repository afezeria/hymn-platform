package github.afezeria.hymn.oss.module.service.impl

import github.afezeria.hymn.oss.module.entity.PreSignedHistory
import github.afezeria.hymn.oss.module.dao.PreSignedHistoryDao
import github.afezeria.hymn.oss.module.dto.PreSignedHistoryDto
import github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class PreSignedHistoryServiceImpl : PreSignedHistoryService {

    @Autowired
    private lateinit var preSignedHistoryDao: PreSignedHistoryDao

    @Autowired
    private lateinit var dbService: DataBaseService

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

}