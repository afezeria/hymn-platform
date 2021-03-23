package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CustomMenuGroupDao
import com.github.afezeria.hymn.core.module.dto.CustomMenuGroupDto
import com.github.afezeria.hymn.core.module.entity.CustomMenuGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomMenuGroupService {

    @Autowired
    private lateinit var customMenuGroupDao: CustomMenuGroupDao

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomMenuGroup> {
        return customMenuGroupDao.pageSelect(null, pageSize, pageNum)
    }

    fun removeById(id: String): Int {
        customMenuGroupDao.selectById(id)
            ?: throw DataNotFoundException("AccountMenuLayout".msgById(id))
        val i = customMenuGroupDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: CustomMenuGroupDto): Int {
        val e = customMenuGroupDao.selectById(id)
            ?: throw DataNotFoundException("AccountMenuLayout".msgById(id))
        dto.update(e)
        val i = customMenuGroupDao.update(e)
        return i
    }

    fun create(dto: CustomMenuGroupDto): String {
        val e = dto.toEntity()
        val id = customMenuGroupDao.insert(e)
        return id
    }

    fun findAll(): MutableList<CustomMenuGroup> {
        return customMenuGroupDao.selectAll()
    }


    fun findById(id: String): CustomMenuGroup? {
        return customMenuGroupDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<CustomMenuGroup> {
        return customMenuGroupDao.selectByIds(ids)
    }


    fun findByAccountId(
        accountId: String,
    ): MutableList<CustomMenuGroup> {
        return customMenuGroupDao.selectByAccountId(accountId)
    }


}