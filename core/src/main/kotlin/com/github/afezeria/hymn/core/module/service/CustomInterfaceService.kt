package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CustomInterfaceDao
import com.github.afezeria.hymn.core.module.dto.CustomInterfaceDto
import com.github.afezeria.hymn.core.module.entity.CustomInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomInterfaceService {

    @Autowired
    private lateinit var customInterfaceDao: CustomInterfaceDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        customInterfaceDao.selectById(id)
            ?: throw DataNotFoundException("CustomInterface".msgById(id))
        val i = customInterfaceDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: CustomInterfaceDto): Int {
        val e = customInterfaceDao.selectById(id)
            ?: throw DataNotFoundException("CustomInterface".msgById(id))
        dto.update(e)
        val i = customInterfaceDao.update(e)
        return i
    }

    fun create(dto: CustomInterfaceDto): String {
        val e = dto.toEntity()
        val id = customInterfaceDao.insert(e)
        return id
    }

    fun findAll(): MutableList<CustomInterface> {
        return customInterfaceDao.selectAll()
    }


    fun findById(id: String): CustomInterface? {
        return customInterfaceDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<CustomInterface> {
        return customInterfaceDao.selectByIds(ids)
    }


    fun findByApi(
        api: String,
    ): CustomInterface? {
        return customInterfaceDao.selectByApi(api)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomInterface> {
        return customInterfaceDao.pageSelect(null, pageSize, pageNum)
    }


}