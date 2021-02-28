package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.MenuItemPermDao
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
import github.afezeria.hymn.core.module.entity.MenuItemPerm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class MenuItemPermService {

    @Autowired
    private lateinit var menuItemPermDao: MenuItemPermDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        menuItemPermDao.selectById(id)
            ?: throw DataNotFoundException("MenuItemPerm".msgById(id))
        val i = menuItemPermDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: MenuItemPermDto): Int {
        val e = menuItemPermDao.selectById(id)
            ?: throw DataNotFoundException("MenuItemPerm".msgById(id))
        dto.update(e)
        val i = menuItemPermDao.update(e)
        return i
    }

    fun create(dto: MenuItemPermDto): String {
        val e = dto.toEntity()
        val id = menuItemPermDao.insert(e)
        return id
    }

    fun findAll(): MutableList<MenuItemPerm> {
        return menuItemPermDao.selectAll()
    }


    fun findById(id: String): MenuItemPerm? {
        return menuItemPermDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<MenuItemPerm> {
        return menuItemPermDao.selectByIds(ids)
    }


    fun findByRoleId(
        roleId: String,
    ): MutableList<MenuItemPerm> {
        return menuItemPermDao.selectByRoleId(roleId)
    }

    fun findByMenuItemId(
        menuItemId: String,
    ): MutableList<MenuItemPerm> {
        return menuItemPermDao.selectByMenuItemId(menuItemId)
    }

    fun batchCreate(dtoList: List<MenuItemPermDto>): Int {
        return menuItemPermDao.bulkInsert(dtoList.map { it.toEntity() })
    }

    fun batchSave(dtoList: List<MenuItemPermDto>): Int {
        return menuItemPermDao.bulkInsertOrUpdate(dtoList.map { it.toEntity() })
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<MenuItemPerm> {
        return menuItemPermDao.pageSelect(null, pageSize, pageNum)
    }

}