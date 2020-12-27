package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.MenuItemPerm
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface MenuItemPermService {

    fun removeById(id: String): Int

    fun update(id: String, dto: MenuItemPermDto): Int

    fun create(dto: MenuItemPermDto): String

    fun findAll(): MutableList<MenuItemPerm>

    fun findById(id: String): MenuItemPerm?

    fun findByIds(ids: List<String>): MutableList<MenuItemPerm>

    fun findByRoleId(
        roleId: String,
    ): MutableList<MenuItemPerm>

    fun findByMenuItemId(
        menuItemId: String,
    ): MutableList<MenuItemPerm>


    fun batchCreate(dtoList: List<MenuItemPermDto>): MutableList<Int>

    /**
     * insert or update on conflict (roleId,typeId)
     */
    fun batchSave(dtoList: List<MenuItemPermDto>): MutableList<Int>

}