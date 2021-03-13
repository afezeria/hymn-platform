package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.DictItemDao
import com.github.afezeria.hymn.core.module.dto.DictItemDto
import com.github.afezeria.hymn.core.module.entity.DictItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class DictItemService {

    @Autowired
    private lateinit var dictItemDao: DictItemDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        dictItemDao.selectById(id)
            ?: throw DataNotFoundException("DictItem".msgById(id))
        val i = dictItemDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: DictItemDto): Int {
        val e = dictItemDao.selectById(id)
            ?: throw DataNotFoundException("DictItem".msgById(id))
        dto.update(e)
        val i = dictItemDao.update(e)
        return i
    }

    fun create(dto: DictItemDto): String {
        val e = dto.toEntity()
        val id = dictItemDao.insert(e)
        return id
    }

    fun findAll(): MutableList<DictItem> {
        return dictItemDao.selectAll()
    }


    fun findById(id: String): DictItem? {
        return dictItemDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<DictItem> {
        return dictItemDao.selectByIds(ids)
    }


    fun findByDictIdAndCode(
        dictId: String,
        code: String,
    ): DictItem? {
        return dictItemDao.selectByDictIdAndCode(dictId, code)
    }

    fun findByDictId(
        dictId: String,
    ): MutableList<DictItem> {
        return dictItemDao.selectByDictId(dictId)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<DictItem> {
        return dictItemDao.pageSelect(null, pageSize, pageNum)
    }


}