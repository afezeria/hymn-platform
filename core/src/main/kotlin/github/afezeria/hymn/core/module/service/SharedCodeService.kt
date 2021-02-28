package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.SharedCodeDao
import github.afezeria.hymn.core.module.dto.SharedCodeDto
import github.afezeria.hymn.core.module.entity.SharedCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class SharedCodeService {

    @Autowired
    private lateinit var sharedCodeDao: SharedCodeDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        sharedCodeDao.selectById(id)
            ?: throw DataNotFoundException("SharedCode".msgById(id))
        val i = sharedCodeDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: SharedCodeDto): Int {
        val e = sharedCodeDao.selectById(id)
            ?: throw DataNotFoundException("SharedCode".msgById(id))
        dto.update(e)
        val i = sharedCodeDao.update(e)
        return i
    }

    fun create(dto: SharedCodeDto): String {
        val e = dto.toEntity()
        val id = sharedCodeDao.insert(e)
        return id
    }

    fun findAll(): MutableList<SharedCode> {
        return sharedCodeDao.selectAll()
    }


    fun findById(id: String): SharedCode? {
        return sharedCodeDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<SharedCode> {
        return sharedCodeDao.selectByIds(ids)
    }


    fun findByApi(
        api: String,
    ): SharedCode? {
        return sharedCodeDao.selectByApi(api)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<SharedCode> {
        return sharedCodeDao.pageSelect(null, pageSize, pageNum)
    }


}