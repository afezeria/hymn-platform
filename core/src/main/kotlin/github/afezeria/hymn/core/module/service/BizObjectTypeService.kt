package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectTypeDao
import github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import github.afezeria.hymn.core.module.entity.BizObjectType
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypeService {

    @Autowired
    private lateinit var bizObjectTypeDao: BizObjectTypeDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectTypeDao.selectAvailableType { it.id eq id }.firstOrNull()
            ?: throw DataNotFoundException("BizObjectType".msgById(id))
        val i = bizObjectTypeDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectTypeDto): Int {
        return dbService.useTransaction {
            val e = bizObjectTypeDao.selectAvailableType { it.id eq id }.firstOrNull()
                ?: throw DataNotFoundException("BizObjectType".msgById(id))
            dto.update(e)
            val i = bizObjectTypeDao.update(e)

            i
        }
    }

    fun create(dto: BizObjectTypeDto): String {
        return dbService.useTransaction {
            val e = dto.toEntity()
            val id = bizObjectTypeDao.insert(e)

            id
        }
    }

    fun findById(id: String): BizObjectType? {
        return bizObjectTypeDao.selectAvailableType { it.id eq id }.firstOrNull()
    }

    fun findByIds(ids: Collection<String>): MutableList<BizObjectType> {
        return bizObjectTypeDao.selectAvailableType { it.id inList ids }
    }

    fun findAvailableTypeByBizObjectId(bizObjectId: String): List<BizObjectType> {
        return bizObjectTypeDao.selectAvailableTypeByBizObjectId(bizObjectId)
    }
}