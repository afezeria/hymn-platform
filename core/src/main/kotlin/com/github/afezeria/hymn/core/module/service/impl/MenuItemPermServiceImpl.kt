package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.MenuItemPermDao
import com.github.afezeria.hymn.core.module.dto.MenuItemPermDto
import com.github.afezeria.hymn.core.module.service.CustomMenuItemService
import com.github.afezeria.hymn.core.module.service.MenuItemPermService
import com.github.afezeria.hymn.core.module.service.RoleService
import com.github.afezeria.hymn.core.module.view.MenuItemPermListView
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class MenuItemPermServiceImpl : MenuItemPermService {

    @Autowired
    private lateinit var menuItemPermDao: MenuItemPermDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var menuItemService: CustomMenuItemService

    @Autowired
    private lateinit var dbService: DatabaseService

    override fun findViewByItemId(menuItemId: String): List<MenuItemPermListView> {
        val menuItem = menuItemService.findById(menuItemId)
            ?: throw DataNotFoundException("CustomMenuItem".msgById(menuItemId))
        val viewRoleIdMap =
            menuItemPermDao.selectView { it.roleId eq menuItemId }
                .map { it.roleId to it }.toMap()
        val menuItemName: String = menuItem.name
        val menuItemApi: String = menuItem.api
        return roleService.findAll()
            .map {
                viewRoleIdMap[it.id] ?: MenuItemPermListView(
                    roleId = it.id,
                    roleName = it.name,
                    menuItemId = menuItemId,
                    menuItemName = menuItemName,
                    menuItemApi = menuItemApi,
                )
            }
    }

    override fun findViewByRoleId(roleId: String): List<MenuItemPermListView> {
        val role = roleService.findById(roleId)
            ?: throw DataNotFoundException("Role".msgById(roleId))
        val viewItemIdMap =
            menuItemPermDao.selectView { it.roleId eq roleId }
                .map { it.menuItemId to it }.toMap()
        val roleName = role.name
        return menuItemService.findAll()
            .map {
                viewItemIdMap[it.id] ?: MenuItemPermListView(
                    roleId = roleId,
                    roleName = roleName,
                    menuItemId = it.id,
                    menuItemName = it.name,
                    menuItemApi = it.api,
                )
            }
    }

    override fun save(dtoList: List<MenuItemPermDto>): Int {
        if (dtoList.isEmpty()) return 0
        return dbService.useTransaction {
            val inRoleIdSet = dtoList.mapTo(mutableSetOf()) { it.roleId }
            val availableRoleIdSet =
                roleService.findByIds(inRoleIdSet).mapTo(mutableSetOf()) { it.id }
//            menu_item_id为关联到core_custom_menu_item表的外键，所有这里不需要对输入的menu_item_id做校验
            val entityList = dtoList.mapNotNull {
                if (availableRoleIdSet.contains(it.roleId)) {
                    it.toEntity()
                } else {
                    null
                }
            }
            menuItemPermDao.bulkInsertOrUpdate(
                entityList,
                *menuItemPermDao.table.run {
                    arrayOf(roleId, menuItemId)
                }
            )
        }
    }

}