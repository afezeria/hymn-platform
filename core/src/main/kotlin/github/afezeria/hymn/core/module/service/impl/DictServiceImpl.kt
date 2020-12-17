package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.Dict
import github.afezeria.hymn.core.module.dao.DictDao
import github.afezeria.hymn.core.module.dto.DictDto
import github.afezeria.hymn.core.module.service.DictService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class DictServiceImpl : DictService {

    @Autowired
    private lateinit var dictDao: DictDao


    override fun removeById(id: String): Int {
        dictDao.selectById(id)
            ?: throw DataNotFoundException("Dict".msgById(id))
        val i = dictDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: DictDto): Int {
        val e = dictDao.selectById(id)
            ?: throw DataNotFoundException("Dict".msgById(id))
        dto.update(e)
        val i = dictDao.update(e)
        return i
    }

    override fun create(dto: DictDto): String {
        val e = dto.toEntity()
        val id = dictDao.insert(e)
        return id
    }

    override fun findAll(): List<Dict> {
        return dictDao.selectAll()
    }


    override fun findById(id: String): Dict? {
        return dictDao.selectById(id)
    }

    override fun findByApi(
        api: String,
    ): Dict? {
        return dictDao.selectByApi(api,)
    }


}