package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BizObjectTypeLayoutDao
import com.github.afezeria.hymn.core.module.dto.BizObjectTypeLayoutDto
import com.github.afezeria.hymn.core.module.view.BizObjectTypeLayoutListView
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypeLayoutService {

    @Autowired
    private lateinit var bizObjectTypeLayoutDao: BizObjectTypeLayoutDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var typeService: BizObjectTypeService

    @Autowired
    private lateinit var layoutService: BizObjectLayoutService

    @Autowired
    private lateinit var bizObjectService: BizObjectService

    @Autowired
    private lateinit var dbService: DatabaseService

    fun findLayoutIdByRoleIdAndTypeId(roleId: String, typeId: String): String {
        roleService.findById(roleId) ?: throw DataNotFoundException("Role".msgById(roleId))
        val type = typeService.findAvailableById(typeId)
            ?: throw DataNotFoundException("BizObjectType".msgById(typeId))
        return bizObjectTypeLayoutDao.singleRowSelect({ (it.roleId eq roleId) and (it.typeId eq typeId) })?.id
            ?: type.defaultLayoutId
    }

    fun findViewByBizObjectId(bizObjectId: String): MutableList<BizObjectTypeLayoutListView> {
        val allRole = roleService.findAll()
        val allType = typeService.findAvailableTypeByBizObjectId(bizObjectId)
        if (allType.isEmpty()) return ArrayList()
        val layoutMap =
            layoutService.findListViewByBizObjectId(bizObjectId).map { it.id to it }.toMap()

        // map<roleId,map<typeId,dto>>
        val dtoMap = mutableMapOf<String, MutableMap<String, BizObjectTypeLayoutListView>>()
        bizObjectTypeLayoutDao.selectView { it.bizObjectId eq bizObjectId }
            .forEach {
                dtoMap.getOrPut(it.roleId) { mutableMapOf() }[it.typeId] = it
            }

        val result = mutableListOf<BizObjectTypeLayoutListView>()
        for (role in allRole) {
            val typeId2dto = dtoMap[role.id] ?: emptyMap()
            for (type in allType) {
                result.add(
                    typeId2dto[type.id] ?: BizObjectTypeLayoutListView(
                        roleId = role.id,
                        typeId = type.id,
                        layoutId = type.defaultLayoutId,
                        roleName = role.name,
                        typeName = type.name,
                        layoutName = requireNotNull(layoutMap[type.defaultLayoutId]).name,
                    )
                )
            }
        }
        return result
    }

    fun save(dtoList: List<BizObjectTypeLayoutDto>): Int {
        if (dtoList.isEmpty()) return 0
        val objectIdSet = mutableSetOf<String>()
        for (dto in dtoList) {
            objectIdSet.add(dto.bizObjectId)
        }
        if (objectIdSet.size != 1) {
            throw BusinessException("不能同时更新多个对象的 类型-页面布局 映射关系")
        }
        bizObjectService.findActiveObjectById(dtoList[0].bizObjectId)
            ?: throw DataNotFoundException("业务对象".msgById(dtoList[0].bizObjectId))
        return bizObjectTypeLayoutDao.bulkInsertOrUpdate(dtoList.map { it.toEntity() },
            *bizObjectTypeLayoutDao.table.run {
                arrayOf(roleId, typeId, layoutId)
            }
        )
    }
}