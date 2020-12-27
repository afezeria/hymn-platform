package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.DictItemDao
import github.afezeria.hymn.core.module.dto.DictItemDto
import github.afezeria.hymn.core.module.entity.DictItem
import github.afezeria.hymn.core.module.service.DictItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class DictItemServiceImpl : DictItemService {

    @Autowired
    private lateinit var dictItemDao: DictItemDao

    @Autowired
    private lateinit var dbService: DataBaseService


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

    override fun findAll(): MutableList<DictItem> {
        return dictItemDao.selectAll()
    }


    override fun findById(id: String): DictItem? {
        return dictItemDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<DictItem> {
        return dictItemDao.selectByIds(ids)
    }


    override fun findByDictIdAndCode(
        dictId: String,
        code: String,
    ): DictItem? {
        return dictItemDao.selectByDictIdAndCode(dictId, code)
    }

    override fun findByDictId(
        dictId: String,
    ): MutableList<DictItem> {
        return dictItemDao.selectByDictId(dictId)
    }


}