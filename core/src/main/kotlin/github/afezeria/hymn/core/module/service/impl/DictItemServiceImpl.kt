package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.DictItem
import github.afezeria.hymn.core.module.dao.DictItemDao
import github.afezeria.hymn.core.module.dto.DictItemDto
import github.afezeria.hymn.core.module.service.DictItemService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class DictItemServiceImpl : DictItemService {

    @Autowired
    lateinit var dictItemDao: DictItemDao


    override fun removeById(id: String): Int {
        dictItemDao.selectById(id)
            ?: throw DataNotFoundException("DictItem".msgById(id))
        val i = dictItemDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: DictItemDto): Int {
        val e = dictItemDao.selectById(id)
            ?: throw DataNotFoundException("DictItem".msgById(id))
        dto.update(e)
        val i = dictItemDao.update(e)
        return i
    }

    override fun create(dto: DictItemDto): String {
        val e = dto.toEntity()
        val id = dictItemDao.insert(e)
        return id
    }

    override fun findAll(): List<DictItem> {
        return dictItemDao.selectAll()
    }


    override fun findById(id: String): DictItem? {
        return dictItemDao.selectById(id)
    }

    override fun findByDictIdAndCode(
        dictId: String,
        code: String,
    ): DictItem? {
        return dictItemDao.selectByDictIdAndCode(dictId,code,)
    }

    override fun findByDictId(
        dictId: String,
    ): List<DictItem> {
        return dictItemDao.selectByDictId(dictId,)
    }


}