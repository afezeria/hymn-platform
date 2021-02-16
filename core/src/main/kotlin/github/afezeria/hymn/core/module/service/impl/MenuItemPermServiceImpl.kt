package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.MenuItemPermDao
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
import github.afezeria.hymn.core.module.entity.MenuItemPerm
import github.afezeria.hymn.core.module.service.MenuItemPermService
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
    private lateinit var dbService: DatabaseService


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

    override fun findAll(): MutableList<MenuItemPerm> {
        return menuItemPermDao.selectAll()
    }


    override fun findById(id: String): MenuItemPerm? {
        return menuItemPermDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<MenuItemPerm> {
        return menuItemPermDao.selectByIds(ids)
    }


    override fun findByRoleId(
        roleId: String,
    ): MutableList<MenuItemPerm> {
        return menuItemPermDao.selectByRoleId(roleId)
    }

    override fun findByMenuItemId(
        menuItemId: String,
    ): MutableList<MenuItemPerm> {
        return menuItemPermDao.selectByMenuItemId(menuItemId)
    }

    override fun batchCreate(dtoList: List<MenuItemPermDto>): Int {
        return menuItemPermDao.bulkInsert(dtoList.map { it.toEntity() })
    }

    override fun batchSave(dtoList: List<MenuItemPermDto>): Int {
        return menuItemPermDao.bulkInsertOrUpdate(dtoList.map { it.toEntity() })
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<MenuItemPerm> {
        return menuItemPermDao.pageSelect(null, pageSize, pageNum)
    }

}