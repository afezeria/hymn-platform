package github.afezeria.hymn.oss.module.service.impl

import github.afezeria.hymn.oss.module.entity.PreSignedHistory
import github.afezeria.hymn.oss.module.dao.PreSignedHistoryDao
import github.afezeria.hymn.oss.module.dto.PreSignedHistoryDto
import github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.exception.DataNotFoundException
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
    private lateinit var dbService: DatabaseService


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
        return preSignedHistoryDao.selectByFileId(fileId,)
    }


}