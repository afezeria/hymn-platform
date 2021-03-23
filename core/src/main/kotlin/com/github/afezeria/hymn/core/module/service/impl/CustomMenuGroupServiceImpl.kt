package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.CustomMenuGroupDao
import com.github.afezeria.hymn.core.module.dto.CustomMenuGroupDto
import com.github.afezeria.hymn.core.module.entity.CustomMenuGroup
import com.github.afezeria.hymn.core.module.service.CustomMenuGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomMenuGroupServiceImpl : CustomMenuGroupService {

    @Autowired
    private lateinit var customMenuGroupDao: CustomMenuGroupDao

    override fun removeById(id: String): Int {
        customMenuGroupDao.selectById(id)
            ?: throw DataNotFoundException("AccountMenuLayout".msgById(id))
        val i = customMenuGroupDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CustomMenuGroupDto): Int {
        val e = customMenuGroupDao.selectById(id)
            ?: throw DataNotFoundException("AccountMenuLayout".msgById(id))
        dto.update(e)
        val i = customMenuGroupDao.update(e)
        return i
    }

    override fun create(dto: CustomMenuGroupDto): String {
        val e = dto.toEntity()
        val id = customMenuGroupDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<CustomMenuGroup> {
        return customMenuGroupDao.selectAll()
    }


    override fun findById(id: String): CustomMenuGroup? {
        return customMenuGroupDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<CustomMenuGroup> {
        return customMenuGroupDao.selectByIds(ids)
    }

}