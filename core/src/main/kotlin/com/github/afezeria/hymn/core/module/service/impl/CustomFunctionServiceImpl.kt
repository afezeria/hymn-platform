package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CustomFunctionDao
import com.github.afezeria.hymn.core.module.dto.CustomFunctionDto
import com.github.afezeria.hymn.core.module.entity.CustomFunction
import com.github.afezeria.hymn.core.module.service.CustomFunctionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomFunctionServiceImpl : CustomFunctionService {

    @Autowired
    private lateinit var customFunction: CustomFunctionDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        customFunction.selectById(id)
            ?: throw DataNotFoundException("SharedCode".msgById(id))
        val i = customFunction.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CustomFunctionDto): Int {
        val e = customFunction.selectById(id)
            ?: throw DataNotFoundException("SharedCode".msgById(id))
        dto.update(e)
        val i = customFunction.update(e)
        return i
    }

    override fun create(dto: CustomFunctionDto): String {
        val e = dto.toEntity()
        val id = customFunction.insert(e)
        return id
    }

    override fun findAll(): MutableList<CustomFunction> {
        return customFunction.selectAll()
    }


    override fun findById(id: String): CustomFunction? {
        return customFunction.selectById(id)
    }

    override fun findByIds(ids: Collection<String>): MutableList<CustomFunction> {
        return customFunction.selectByIds(ids)
    }


    override fun findByApi(
        api: String,
    ): CustomFunction? {
        return customFunction.selectByApi(api)
    }

    override fun pageFind(
        pageSize: Int,
        pageNum: Int
    ): List<CustomFunction> {
        return customFunction.pageSelect(null, pageSize, pageNum)
    }


}