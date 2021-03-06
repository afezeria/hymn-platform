package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.dao.BizObjectTypePermDao
import com.github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import com.github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import com.github.afezeria.hymn.core.module.service.BizObjectTypePermService
import com.github.afezeria.hymn.core.module.service.BizObjectTypeService
import com.github.afezeria.hymn.core.module.service.RoleService
import com.github.afezeria.hymn.core.module.view.BizObjectTypePermListView
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypePermServiceImpl : BizObjectTypePermService {

    @Autowired
    private lateinit var bizObjectTypePermDao: BizObjectTypePermDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var typeService: BizObjectTypeService

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun save(dtoList: List<BizObjectTypePermDto>): Int {
        if (dtoList.isEmpty()) return 0
        return dbService.useTransaction {
            val inTypeIdSet = mutableSetOf<String>()
            val inRoleIdSet = mutableSetOf<String>()
            dtoList.forEach {
                inTypeIdSet.add(it.typeId)
                inRoleIdSet.add(it.roleId)
            }
            val availableRoleIdSet =
                roleService.findByIds(inRoleIdSet).mapTo(mutableSetOf()) { it.id }
            val availableTypeIdSet =
                typeService.findAvailableByIds(inTypeIdSet).mapTo(mutableSetOf()) { it.id }
            val entityList = dtoList.mapNotNull {
                if (availableTypeIdSet.contains(it.typeId) &&
                    availableRoleIdSet.contains(it.roleId)
                ) {
                    it.toEntity()
                } else {
                    null
                }
            }
            bizObjectTypePermDao.bulkInsertOrUpdate(
                entityList,
                *bizObjectTypePermDao.table.run { arrayOf(roleId, typeId) }
            )
        }

    }

    override fun findViewByTypeId(typeId: String): List<BizObjectTypePermListView> {
        val viewRoleIdMap =
            bizObjectTypePermDao.selectView { it, _ -> it.typeId eq typeId }
                .map { it.roleId to it }.toMap()
        val objectId = viewRoleIdMap.values.first().bizObjectId
        val typeName = viewRoleIdMap.values.first().typeName
        return roleService.findAll()
            .map {
                viewRoleIdMap[it.id] ?: BizObjectTypePermListView(
                    roleId = it.id,
                    roleName = it.name,
                    typeId = typeId,
                    typeName = typeName,
                    bizObjectId = objectId,
                )
            }

    }

    override fun findViewByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): List<BizObjectTypePermListView> {
        val dtoTypeIdMap =
            bizObjectTypePermDao.selectView { perms, types ->
                (perms.roleId eq roleId) and (types.bizObjectId eq bizObjectId)
            }
                .map { it.typeId to it }.toMap()
        val roleName = dtoTypeIdMap.values.first().roleName
        return typeService.findAvailableTypeByBizObjectId(bizObjectId)
            .map {
                dtoTypeIdMap[it.id] ?: BizObjectTypePermListView(
                    roleId = roleId,
                    roleName = roleName,
                    typeId = it.id,
                    typeName = it.name,
                    bizObjectId = bizObjectId,
                )
            }

    }

    override fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): MutableList<BizObjectTypePerm> {
        return bizObjectTypePermDao.findByRoleIdAndBizObjectId(roleId, bizObjectId)
    }


}