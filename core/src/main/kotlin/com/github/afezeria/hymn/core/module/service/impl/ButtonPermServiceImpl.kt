package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.ButtonPermDao
import com.github.afezeria.hymn.core.module.dto.ButtonPermDto
import com.github.afezeria.hymn.core.module.service.BizObjectService
import com.github.afezeria.hymn.core.module.service.ButtonPermService
import com.github.afezeria.hymn.core.module.service.CustomButtonService
import com.github.afezeria.hymn.core.module.service.RoleService
import com.github.afezeria.hymn.core.module.view.ButtonPermListView
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.isNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ButtonPermServiceImpl : ButtonPermService {

    @Autowired
    private lateinit var buttonPermDao: ButtonPermDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var buttonService: CustomButtonService

    @Autowired
    private lateinit var objectService: BizObjectService

    @Autowired
    private lateinit var dbService: DatabaseService

    override fun findViewByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): List<ButtonPermListView> {
        roleService.findById(roleId) ?: throw DataNotFoundException("Role".msgById(roleId))
        objectService.findActiveObjectById(bizObjectId)
            ?: throw DataNotFoundException("BizObject".msgById(roleId))
        val viewList = buttonPermDao.selectView { perms, buttons ->
            (perms.roleId eq roleId) and (buttons.bizObjectId eq bizObjectId)
        }
        if (viewList.isEmpty()) return emptyList()
        return viewList
    }

    override fun findViewByButtonId(buttonId: String): List<ButtonPermListView> {
        val button = buttonService.findById(buttonId)
            ?: throw DataNotFoundException("CustomButton".msgById(buttonId))
        val viewRoleIdMap =
            buttonPermDao.selectView { it, _ -> it.buttonId eq buttonId }
                .map { it.roleId to it }.toMap()
        val objectId: String? = button.bizObjectId
        var objectName: String? = null
        if (objectId != null) {
            val bizObject = objectService.findActiveObjectById(objectId)
                ?: throw DataNotFoundException("BizObject".msgById(objectId))
            objectName = bizObject.name
        }
        return roleService.findAll()
            .map {
                viewRoleIdMap[it.id] ?: ButtonPermListView(
                    roleId = it.id,
                    roleName = it.name,
                    buttonId = buttonId,
                    buttonName = button.name,
                    bizObjectName = objectName,
                    bizObjectId = objectId,
                )
            }
    }

    /**
     * 根据角色id查找通用按钮的权限
     */
    override fun findViewByRoleId(roleId: String): List<ButtonPermListView> {
        val role =
            roleService.findById(roleId) ?: throw DataNotFoundException("Role".msgById(roleId))
        val viewButtonIdMap =
            buttonPermDao.selectView { perms, buttons ->
                (perms.roleId eq roleId) and (buttons.bizObjectId.isNull())
            }
                .map { it.buttonId to it }.toMap()
        val roleName: String = role.name
        return buttonService.findAll()
            .map {
                viewButtonIdMap[it.id] ?: ButtonPermListView(
                    roleId = roleId,
                    roleName = roleName,
                    buttonId = it.id,
                    buttonName = it.name,
                )
            }
    }

    override fun save(dtoList: List<ButtonPermDto>): Int {
        if (dtoList.isEmpty()) return 0
        return dbService.useTransaction {
            val inRoleIdSet = mutableSetOf<String>()
            val inButtonIdSet = mutableSetOf<String>()
            dtoList.forEach {
                inRoleIdSet.add(it.roleId)
                inButtonIdSet.add(it.buttonId)
            }
            val availableRoleIdSet =
                roleService.findByIds(inRoleIdSet).mapTo(mutableSetOf()) { it.id }
            val buttonList = buttonService.findByIds(inButtonIdSet)
            val objectIds = buttonList.mapNotNull { it.bizObjectId }
            val activeObjectIdSet =
                objectService.findActiveObjectByIds(objectIds).mapTo(mutableSetOf()) { it.id }
            val availableButtonIdSet =
                buttonList.mapNotNull {
                    if (it.bizObjectId == null || activeObjectIdSet.contains(it.bizObjectId)) it.id
                    else null
                }
            val entityList = dtoList.mapNotNull {
                if (availableRoleIdSet.contains(it.roleId) &&
                    availableButtonIdSet.contains(it.buttonId)
                ) {
                    it.toEntity()
                } else {
                    null
                }
            }
            buttonPermDao.bulkInsertOrUpdate(
                entityList,
                *buttonPermDao.table.run {
                    arrayOf(roleId, buttonId)
                }
            )
        }
    }
}