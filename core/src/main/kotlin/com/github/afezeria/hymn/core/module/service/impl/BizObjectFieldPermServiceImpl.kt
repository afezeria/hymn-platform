package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.dao.BizObjectFieldPermDao
import com.github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import com.github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import com.github.afezeria.hymn.core.module.service.BizObjectFieldPermService
import com.github.afezeria.hymn.core.module.service.BizObjectFieldService
import com.github.afezeria.hymn.core.module.service.RoleService
import com.github.afezeria.hymn.core.module.view.BizObjectFieldPermListView
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectFieldPermServiceImpl : BizObjectFieldPermService {

    @Autowired
    private lateinit var bizObjectFieldPermDao: BizObjectFieldPermDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var fieldService: BizObjectFieldService

    @Autowired
    private lateinit var dbService: DatabaseService

    override fun save(dtoList: List<BizObjectFieldPermDto>): Int {
        if (dtoList.isEmpty()) return 0
        return dbService.useTransaction {
            val inFieldIdSet = mutableSetOf<String>()
            val inRoleIdSet = mutableSetOf<String>()
            dtoList.forEach {
                inFieldIdSet.add(it.fieldId)
                inRoleIdSet.add(it.roleId)
            }
            val availableRoleIdSet =
                roleService.findByIds(inRoleIdSet).mapTo(mutableSetOf()) { it.id }
            val availableFieldIdSet =
                fieldService.findByIds(inFieldIdSet).mapTo(mutableSetOf()) { it.id }
            val entityList = dtoList.mapNotNull {
                if (availableFieldIdSet.contains(it.fieldId) &&
                    availableRoleIdSet.contains(it.roleId)
                ) {
                    it.toEntity()
                } else {
                    null
                }
            }
            bizObjectFieldPermDao.bulkInsertOrUpdate(
                entityList,
                *bizObjectFieldPermDao.table.run { arrayOf(roleId, fieldId) }
            )
        }
    }

    override fun findViewByFieldId(fieldId: String): List<BizObjectFieldPermListView> {
        val viewRoleIdMap =
            bizObjectFieldPermDao.selectView { it, _ -> it.fieldId eq fieldId }
                .map { it.roleId to it }.toMap()
        val fieldName = viewRoleIdMap.values.first().fieldName
        val objectId = viewRoleIdMap.values.first().bizObjectId
        return roleService.findAll()
            .map {
                viewRoleIdMap[it.id] ?: BizObjectFieldPermListView(
                    roleId = it.id, fieldId = fieldId,
                    roleName = it.name,
                    bizObjectId = objectId,
                    fieldName = fieldName,
                )
            }
    }


    override fun findViewByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): List<BizObjectFieldPermListView> {
        val viewFieldIdMap =
            bizObjectFieldPermDao.selectView { perm, field -> (perm.roleId eq roleId) and (field.bizObjectId eq bizObjectId) }
                .map { it.fieldId to it }.toMap()
        val roleName = viewFieldIdMap.values.first().roleName
        return fieldService.findByBizObjectId(bizObjectId)
            .map {
                viewFieldIdMap[it.id] ?: BizObjectFieldPermListView(
                    roleId = roleId, fieldId = it.id,
                    roleName = roleName,
                    bizObjectId = bizObjectId,
                    fieldName = it.name,
                )
            }

    }


    override fun findByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm? {
        return bizObjectFieldPermDao.selectByRoleIdAndFieldId(roleId, fieldId)
    }
}