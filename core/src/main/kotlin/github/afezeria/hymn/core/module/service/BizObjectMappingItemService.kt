package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectMappingItemDao
import github.afezeria.hymn.core.module.dto.BizObjectMappingItemDto
import github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectMappingItemService {

    @Autowired
    private lateinit var bizObjectMappingItemDao: BizObjectMappingItemDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectMappingItemDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectMappingItem".msgById(id))
        val i = bizObjectMappingItemDao.deleteById(id)
        return i
    }

    /**
     * 更新对象映射表
     * 更新前删除旧数据
     */
    fun save(dtoList: List<BizObjectMappingItemDto>): Int {
        if (dtoList.isEmpty()) return 0
        val mappingIdSet = dtoList.mapTo(mutableSetOf()) { it.mappingId }
        if (mappingIdSet.size != 1) throw BusinessException("不能同时修改多个对象映射关系")
        return dbService.useTransaction {
            bizObjectMappingItemDao.delete { it.mappingId eq mappingIdSet.first() }
            val entityList = dtoList.map { it.toEntity() }
            bizObjectMappingItemDao.batchSave(entityList)
        }
    }

    fun create(dto: BizObjectMappingItemDto): String {
        val e = dto.toEntity()
        val id = bizObjectMappingItemDao.insert(e)
        return id
    }

    fun findByMappingId(mappingId: String): MutableList<BizObjectMappingItem> {
        return bizObjectMappingItemDao.select({ it.mappingId eq mappingId })
    }

}