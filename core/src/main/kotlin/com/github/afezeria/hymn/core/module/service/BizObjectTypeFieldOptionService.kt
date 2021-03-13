package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BizObjectTypeFieldOptionDao
import com.github.afezeria.hymn.core.module.dto.BizObjectTypeFieldOptionDto
import com.github.afezeria.hymn.core.module.entity.BizObjectTypeFieldOption
import com.github.afezeria.hymn.core.module.view.BizObjectTypeFieldOptionListView
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypeFieldOptionService {

    @Autowired
    private lateinit var bizObjectTypeFieldOptionDao: BizObjectTypeFieldOptionDao

    @Autowired
    private lateinit var fieldService: BizObjectFieldService

    @Autowired
    private lateinit var typeService: BizObjectTypeService

    @Autowired
    private lateinit var dictItemService: DictItemService


    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectTypeFieldOptionDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypeOptions".msgById(id))
        val i = bizObjectTypeFieldOptionDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectTypeFieldOptionDto): Int {
        val e = bizObjectTypeFieldOptionDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypeOptions".msgById(id))
        dto.update(e)
        val i = bizObjectTypeFieldOptionDao.update(e)
        return i
    }

    fun create(dto: BizObjectTypeFieldOptionDto): String {
        val e = dto.toEntity()
        val id = bizObjectTypeFieldOptionDao.insert(e)
        return id
    }

    fun findAll(): MutableList<BizObjectTypeFieldOption> {
        return bizObjectTypeFieldOptionDao.selectAll()
    }


    fun findById(id: String): BizObjectTypeFieldOption? {
        return bizObjectTypeFieldOptionDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<BizObjectTypeFieldOption> {
        return bizObjectTypeFieldOptionDao.selectByIds(ids)
    }


    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectTypeFieldOption> {
        return bizObjectTypeFieldOptionDao.selectByBizObjectId(bizObjectId)
    }

    fun findByTypeId(
        typeId: String,
    ): MutableList<BizObjectTypeFieldOption> {
        return bizObjectTypeFieldOptionDao.selectByTypeId(typeId)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectTypeFieldOption> {
        return bizObjectTypeFieldOptionDao.pageSelect(null, pageSize, pageNum)
    }

    fun findView(typeId: String, fieldIds: List<String>): List<BizObjectTypeFieldOptionListView> {
        return if (fieldIds.isEmpty()) {
            bizObjectTypeFieldOptionDao.selectView { (it.typeId eq typeId) }
        } else {
            bizObjectTypeFieldOptionDao.selectView { (it.typeId eq typeId) and (it.fieldId inList fieldIds) }
        }
    }

    fun save(dtoList: List<BizObjectTypeFieldOptionDto>): Int {
        if (dtoList.isEmpty()) return 0
        return dbService.useTransaction {
            val fieldId = dtoList.first().fieldId
            val field = fieldService.findById(fieldId)
                ?: throw DataNotFoundException("BizObjectField".msgById(fieldId))
            if (field.type != "check_box_group" && field.type != "select") {
                throw BusinessException("字段 [id:$fieldId] 不是多选字段")
            }
            val objectId = field.bizObjectId

            val typeId = dtoList.first().typeId
            typeService.findAvailableById(typeId)
                ?: throw DataNotFoundException("BizObjectType".msgById(typeId))

            val dictId = requireNotNull(field.dictId)
            val itemIdSet = dictItemService.findByDictId(dictId).map { it.id }
            if (itemIdSet.isEmpty()) {
                throw BusinessException("字典 [id:$dictId] 没有可选项")
            }
            bizObjectTypeFieldOptionDao.delete { (it.typeId eq typeId) and (it.fieldId eq fieldId) }
            bizObjectTypeFieldOptionDao.bulkInsert(dtoList.mapNotNull {
                if (itemIdSet.contains(it.dictItemId)) {
                    BizObjectTypeFieldOption(
                        bizObjectId = objectId,
                        typeId = typeId,
                        fieldId = fieldId,
                        dictItemId = it.dictItemId
                    )
                } else null
            })
        }
    }


}