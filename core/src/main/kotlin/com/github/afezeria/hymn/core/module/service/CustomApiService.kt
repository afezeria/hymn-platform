package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CustomApiDao
import com.github.afezeria.hymn.core.module.dto.CustomInterfaceDto
import com.github.afezeria.hymn.core.module.entity.CustomApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomApiService {

    @Autowired
    private lateinit var customApiDao: CustomApiDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        customApiDao.selectById(id)
            ?: throw DataNotFoundException("CustomInterface".msgById(id))
        val i = customApiDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: CustomInterfaceDto): Int {
        val e = customApiDao.selectById(id)
            ?: throw DataNotFoundException("CustomInterface".msgById(id))
        dto.update(e)
        val i = customApiDao.update(e)
        return i
    }

    fun create(dto: CustomInterfaceDto): String {
        val e = dto.toEntity()
        val id = customApiDao.insert(e)
        return id
    }

    fun findAll(): MutableList<CustomApi> {
        return customApiDao.selectAll()
    }


    fun findById(id: String): CustomApi? {
        return customApiDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<CustomApi> {
        return customApiDao.selectByIds(ids)
    }


    fun findByApi(
        api: String,
    ): CustomApi? {
        return customApiDao.selectByApi(api)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomApi> {
        return customApiDao.pageSelect(null, pageSize, pageNum)
    }


}