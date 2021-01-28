package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.SharedCodeDao
import github.afezeria.hymn.core.module.dto.SharedCodeDto
import github.afezeria.hymn.core.module.entity.SharedCode
import github.afezeria.hymn.core.module.service.SharedCodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class SharedCodeServiceImpl : SharedCodeService {

    @Autowired
    private lateinit var sharedCodeDao: SharedCodeDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        sharedCodeDao.selectById(id)
            ?: throw DataNotFoundException("SharedCode".msgById(id))
        val i = sharedCodeDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: SharedCodeDto): Int {
        val e = sharedCodeDao.selectById(id)
            ?: throw DataNotFoundException("SharedCode".msgById(id))
        dto.update(e)
        val i = sharedCodeDao.update(e)
        return i
    }

    override fun create(dto: SharedCodeDto): String {
        val e = dto.toEntity()
        val id = sharedCodeDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<SharedCode> {
        return sharedCodeDao.selectAll()
    }


    override fun findById(id: String): SharedCode? {
        return sharedCodeDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<SharedCode> {
        return sharedCodeDao.selectByIds(ids)
    }


    override fun findByApi(
        api: String,
    ): SharedCode? {
        return sharedCodeDao.selectByApi(api)
    }


}