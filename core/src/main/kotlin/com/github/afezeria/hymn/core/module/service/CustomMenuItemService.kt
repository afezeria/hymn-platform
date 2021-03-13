package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CustomMenuItemDao
import com.github.afezeria.hymn.core.module.dto.CustomMenuItemDto
import com.github.afezeria.hymn.core.module.entity.CustomMenuItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomMenuItemService {

    @Autowired
    private lateinit var customMenuItemDao: CustomMenuItemDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        customMenuItemDao.selectById(id)
            ?: throw DataNotFoundException("CustomMenuItem".msgById(id))
        val i = customMenuItemDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: CustomMenuItemDto): Int {
        return dbService.useTransaction {
            val e = customMenuItemDao.selectById(id)
                ?: throw DataNotFoundException("CustomMenuItem".msgById(id))
            dto.update(e)
            val i = customMenuItemDao.update(e)

            i
        }
    }

    fun create(dto: CustomMenuItemDto): String {
        return dbService.useTransaction {
            val e = dto.toEntity()
            val id = customMenuItemDao.insert(e)

            id
        }
    }

    fun findAll(): MutableList<CustomMenuItem> {
        return customMenuItemDao.selectAll()
    }


    fun findById(id: String): CustomMenuItem? {
        return customMenuItemDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<CustomMenuItem> {
        return customMenuItemDao.selectByIds(ids)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomMenuItem> {
        return customMenuItemDao.pageSelect(null, pageSize, pageNum)
    }


}