package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.MenuItemPerm
import github.afezeria.hymn.core.module.dao.MenuItemPermDao
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
import github.afezeria.hymn.core.module.service.MenuItemPermService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class MenuItemPermServiceImpl(
    private val menuItemPermDao: MenuItemPermDao,
) : MenuItemPermService {
    override fun removeById(id: String): Int {
        menuItemPermDao.selectById(id)
            ?: throw DataNotFoundException("MenuItemPerm".msgById(id))
        val i = menuItemPermDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: MenuItemPermDto): Int {
        val e = menuItemPermDao.selectById(id)
            ?: throw DataNotFoundException("MenuItemPerm".msgById(id))
        dto.update(e)
        val i = menuItemPermDao.update(e)
        return i
    }

    override fun create(dto: MenuItemPermDto): String {
        val e = dto.toEntity()
        val id = menuItemPermDao.insert(e)
        return id
    }

    override fun findAll(): List<MenuItemPerm> {
        return menuItemPermDao.selectAll()
    }


    override fun findById(id: String): MenuItemPerm? {
        return menuItemPermDao.selectById(id)
    }

    override fun findByRoleId(
        roleId: String,
    ): List<MenuItemPerm> {
        return menuItemPermDao.selectByRoleId(roleId,)
    }

    override fun findByMenuItemId(
        menuItemId: String,
    ): List<MenuItemPerm> {
        return menuItemPermDao.selectByMenuItemId(menuItemId,)
    }


}