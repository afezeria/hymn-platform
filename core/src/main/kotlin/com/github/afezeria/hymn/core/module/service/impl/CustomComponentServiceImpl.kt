package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CustomComponentDao
import com.github.afezeria.hymn.core.module.dto.CustomComponentDto
import com.github.afezeria.hymn.core.module.entity.CustomComponent
import com.github.afezeria.hymn.core.module.service.CustomComponentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomComponentServiceImpl : CustomComponentService {

    @Autowired
    private lateinit var customComponentDao: CustomComponentDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        customComponentDao.selectById(id)
            ?: throw DataNotFoundException("CustomComponent".msgById(id))
        val i = customComponentDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CustomComponentDto): Int {
        val e = customComponentDao.selectById(id)
            ?: throw DataNotFoundException("CustomComponent".msgById(id))
        dto.update(e)
        val i = customComponentDao.update(e)
        return i
    }

    override fun create(dto: CustomComponentDto): String {
        val e = dto.toEntity()
        val id = customComponentDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<CustomComponent> {
        return customComponentDao.selectAll()
    }


    override fun findById(id: String): CustomComponent? {
        return customComponentDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<CustomComponent> {
        return customComponentDao.selectByIds(ids)
    }


    override fun findByApi(
        api: String,
    ): CustomComponent? {
        return customComponentDao.selectByApi(api)
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<CustomComponent> {
        return customComponentDao.pageSelect(null, pageSize, pageNum)
    }


}