package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.MenuItemPermDto
import com.github.afezeria.hymn.core.module.view.MenuItemPermListView

/**
 * @author afezeria
 */
interface MenuItemPermService {
    fun findViewByItemId(menuItemId: String): List<MenuItemPermListView>
    fun findViewByRoleId(roleId: String): List<MenuItemPermListView>
    fun save(dtoList: List<MenuItemPermDto>): Int
}