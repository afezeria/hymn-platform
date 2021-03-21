package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CustomFunctionDao
import com.github.afezeria.hymn.core.module.dto.CustomFunctionDto
import com.github.afezeria.hymn.core.module.entity.CustomFunction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomFunctionService {

    @Autowired
    private lateinit var customFunction: CustomFunctionDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        customFunction.selectById(id)
            ?: throw DataNotFoundException("SharedCode".msgById(id))
        val i = customFunction.deleteById(id)
        return i
    }

    fun update(id: String, dto: CustomFunctionDto): Int {
        val e = customFunction.selectById(id)
            ?: throw DataNotFoundException("SharedCode".msgById(id))
        dto.update(e)
        val i = customFunction.update(e)
        return i
    }

    fun create(dto: CustomFunctionDto): String {
        val e = dto.toEntity()
        val id = customFunction.insert(e)
        return id
    }

    fun findAll(): MutableList<CustomFunction> {
        return customFunction.selectAll()
    }


    fun findById(id: String): CustomFunction? {
        return customFunction.selectById(id)
    }

    fun findByIds(ids: Collection<String>): MutableList<CustomFunction> {
        return customFunction.selectByIds(ids)
    }


    fun findByApi(
        api: String,
    ): CustomFunction? {
        return customFunction.selectByApi(api)
    }

    fun pageFind(
        pageSize: Int,
        pageNum: Int
    ): List<CustomFunction> {
        return customFunction.pageSelect(null, pageSize, pageNum)
    }


}