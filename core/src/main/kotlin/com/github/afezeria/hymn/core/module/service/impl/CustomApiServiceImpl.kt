package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CustomApiDao
import com.github.afezeria.hymn.core.module.dto.CustomApiDto
import com.github.afezeria.hymn.core.module.entity.CustomApi
import com.github.afezeria.hymn.core.module.service.CustomApiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomApiServiceImpl : CustomApiService {

    @Autowired
    private lateinit var customApiDao: CustomApiDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        customApiDao.selectById(id)
            ?: throw DataNotFoundException("CustomInterface".msgById(id))
        val i = customApiDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CustomApiDto): Int {
        val e = customApiDao.selectById(id)
            ?: throw DataNotFoundException("CustomInterface".msgById(id))
        dto.update(e)
        val i = customApiDao.update(e)
        return i
    }

    override fun create(dto: CustomApiDto): String {
        val e = dto.toEntity()
        val id = customApiDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<CustomApi> {
        return customApiDao.selectAll()
    }


    override fun findById(id: String): CustomApi? {
        return customApiDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<CustomApi> {
        return customApiDao.selectByIds(ids)
    }


    override fun findByApi(
        api: String,
    ): CustomApi? {
        return customApiDao.selectByApi(api)
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<CustomApi> {
        return customApiDao.pageSelect(null, pageSize, pageNum)
    }


}