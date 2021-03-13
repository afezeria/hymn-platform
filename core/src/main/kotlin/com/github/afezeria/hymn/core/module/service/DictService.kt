package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.DictDao
import com.github.afezeria.hymn.core.module.dto.DictDto
import com.github.afezeria.hymn.core.module.entity.Dict
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class DictService {

    @Autowired
    private lateinit var dictDao: DictDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        dictDao.selectById(id)
            ?: throw DataNotFoundException("Dict".msgById(id))
        val i = dictDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: DictDto): Int {
        val e = dictDao.selectById(id)
            ?: throw DataNotFoundException("Dict".msgById(id))
        dto.update(e)
        val i = dictDao.update(e)
        return i
    }

    fun create(dto: DictDto): String {
        val e = dto.toEntity()
        val id = dictDao.insert(e)
        return id
    }

    fun findAll(): MutableList<Dict> {
        return dictDao.selectAll()
    }


    fun findById(id: String): Dict? {
        return dictDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<Dict> {
        return dictDao.selectByIds(ids)
    }


    fun findByApi(
        api: String,
    ): Dict? {
        return dictDao.selectByApi(api)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<Dict> {
        return dictDao.pageSelect(null, pageSize, pageNum)
    }


}