package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BizObjectLayoutDao
import com.github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import com.github.afezeria.hymn.core.module.entity.BizObjectLayout
import com.github.afezeria.hymn.core.module.view.BizObjectLayoutListView
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * @author afezeria
 */
@Service
class BizObjectLayoutService {

    @Autowired
    private lateinit var bizObjectLayoutDao: BizObjectLayoutDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var bizObjectService: BizObjectService

    @Autowired
    private lateinit var fieldService: BizObjectFieldService

    @Autowired
    private lateinit var buttonService: CustomButtonService

    @Autowired
    private lateinit var typeService: BizObjectTypeService

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectLayoutDao.selectAvailableLayout { it.id eq id }.firstOrNull()
            ?: throw DataNotFoundException("BizObjectLayout".msgById(id))
        val i = bizObjectLayoutDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectLayoutDto): Int {
        val e = bizObjectLayoutDao.selectAvailableLayout { it.id eq id }.firstOrNull()
            ?: throw DataNotFoundException("BizObjectLayout".msgById(id))
        dto.update(e)
        val i = bizObjectLayoutDao.update(e)
        return i
    }

    fun create(dto: BizObjectLayoutDto): String {
        val components = dto.components
        dto.components.apply {
            val objectIdSet = bizObjectService.findActiveObjectByIds(objects.map { it.id })
                .mapTo(mutableSetOf()) { it.id }
            objects.removeAll { !objectIdSet.contains(it.id) }
            for (obj in objects) {
                val fieldIdSet =
                    fieldService.findByBizObjectId(obj.id).mapTo(mutableSetOf()) { it.id }
                obj.fields.removeAll { !fieldIdSet.contains(it.id) }
                val typeIdSet = typeService.findAvailableTypeByBizObjectId(obj.id)
                    .mapTo(mutableSetOf()) { it.id }
                obj.types.removeAll { !typeIdSet.contains(it) }
            }
            val buttonIdSet =
                buttonService.findByIds(components.buttons).mapTo(mutableSetOf()) { it.id }
            components.buttons.removeAll { !buttonIdSet.contains(it) }
        }

        val e = dto.toEntity()
        val id = bizObjectLayoutDao.insert(e)
        return id
    }

    fun findById(id: String): BizObjectLayout? {
        return bizObjectLayoutDao.selectAvailableLayout { it.id eq id }
            .firstOrNull()
    }

    fun findByBizObjectId(bizObjectId: String): MutableList<BizObjectLayout> {
        return bizObjectLayoutDao.select({ it.bizObjectId eq bizObjectId })
    }

    fun findListViewByBizObjectId(bizObjectId: String): MutableList<BizObjectLayoutListView> {
        return bizObjectLayoutDao.selectView { it.bizObjectId eq bizObjectId }
    }

}