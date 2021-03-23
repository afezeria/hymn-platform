package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BizObjectTypeDao
import com.github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import com.github.afezeria.hymn.core.module.entity.BizObjectType
import com.github.afezeria.hymn.core.module.service.BizObjectTypeService
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypeServiceImpl : BizObjectTypeService {

    @Autowired
    private lateinit var bizObjectTypeDao: BizObjectTypeDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        bizObjectTypeDao.selectAvailableType { it.id eq id }.firstOrNull()
            ?: throw DataNotFoundException("BizObjectType".msgById(id))
        val i = bizObjectTypeDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectTypeDto): Int {
        return dbService.useTransaction {
            val e = bizObjectTypeDao.selectAvailableType { it.id eq id }.firstOrNull()
                ?: throw DataNotFoundException("BizObjectType".msgById(id))
            dto.update(e)
            val i = bizObjectTypeDao.update(e)

            i
        }
    }

    override fun create(dto: BizObjectTypeDto): String {
        return dbService.useTransaction {
            val e = dto.toEntity()
            val id = bizObjectTypeDao.insert(e)

            id
        }
    }

    override fun findAvailableById(id: String): BizObjectType? {
        return bizObjectTypeDao.selectAvailableType { it.id eq id }.firstOrNull()
    }

    override fun findAvailableByIds(ids: Collection<String>): MutableList<BizObjectType> {
        return bizObjectTypeDao.selectAvailableType { it.id inList ids }
    }

    override fun findAvailableTypeByBizObjectId(bizObjectId: String): List<BizObjectType> {
        return bizObjectTypeDao.selectAvailableTypeByBizObjectId(bizObjectId)
    }
}