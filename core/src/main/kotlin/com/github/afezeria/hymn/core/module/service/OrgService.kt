package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.OrgDao
import com.github.afezeria.hymn.core.module.dto.OrgDto
import com.github.afezeria.hymn.core.module.entity.Org
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class OrgService {

    @Autowired
    private lateinit var orgDao: OrgDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        orgDao.selectById(id)
            ?: throw DataNotFoundException("Org".msgById(id))
        val i = orgDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: OrgDto): Int {
        val e = orgDao.selectById(id)
            ?: throw DataNotFoundException("Org".msgById(id))
        dto.update(e)
        val i = orgDao.update(e)
        return i
    }

    fun create(dto: OrgDto): String {
        val e = dto.toEntity()
        val id = orgDao.insert(e)
        return id
    }

    fun findAll(): MutableList<Org> {
        return orgDao.selectAll()
    }


    fun findById(id: String): Org? {
        return orgDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<Org> {
        return orgDao.selectByIds(ids)
    }


    fun findByParentId(
        parentId: String,
    ): MutableList<Org> {
        return orgDao.selectByParentId(parentId)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<Org> {
        return orgDao.pageSelect(null, pageSize, pageNum)
    }


}