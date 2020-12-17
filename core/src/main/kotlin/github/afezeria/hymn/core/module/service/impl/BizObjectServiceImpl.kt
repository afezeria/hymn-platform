package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.BizObject
import github.afezeria.hymn.core.module.dao.BizObjectDao
import github.afezeria.hymn.core.module.dto.BizObjectDto
import github.afezeria.hymn.core.module.service.BizObjectService
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class BizObjectServiceImpl : BizObjectService {

    @Autowired
    private lateinit var bizObjectDao: BizObjectDao

    @Autowired
    private lateinit var dbService: DataBaseService


    override fun removeById(id: String): Int {
        bizObjectDao.selectById(id)
            ?: throw DataNotFoundException("BizObject".msgById(id))
        val i = bizObjectDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectDto): Int {
        val e = bizObjectDao.selectById(id)
            ?: throw DataNotFoundException("BizObject".msgById(id))
        dto.update(e)
        val i = bizObjectDao.update(e)
        return i
    }

    override fun create(dto: BizObjectDto): String {
        val e = dto.toEntity()
        val id = bizObjectDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<BizObject> {
        return bizObjectDao.selectAll()
    }


    override fun findById(id: String): BizObject? {
        return bizObjectDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObject> {
        return bizObjectDao.selectByIds(ids)
    }


    override fun findByApi(
        api: String,
    ): BizObject? {
        return bizObjectDao.selectByApi(api,)
    }


}