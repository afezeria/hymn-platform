package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.CustomMenuItemDao
import github.afezeria.hymn.core.module.dto.CustomMenuItemDto
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
import github.afezeria.hymn.core.module.entity.CustomMenuItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomMenuItemService {

    @Autowired
    private lateinit var customMenuItemDao: CustomMenuItemDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var menuItemPermService: MenuItemPermService

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        customMenuItemDao.selectById(id)
            ?: throw DataNotFoundException("CustomMenuItem".msgById(id))
        val i = customMenuItemDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: CustomMenuItemDto): Int {
        return dbService.useTransaction {
            val e = customMenuItemDao.selectById(id)
                ?: throw DataNotFoundException("CustomMenuItem".msgById(id))
            dto.update(e)
            val i = customMenuItemDao.update(e)

            //            创建菜单项权限数据
            val roleIdSet = roleService.findIdList().toMutableSet()
            val menuItemPermDtoList = dto.permList
                .filter { roleIdSet.contains(it.roleId) }
                .onEach { it.menuItemId = id }
            menuItemPermService.batchSave(menuItemPermDtoList)
            i
        }
    }

    fun create(dto: CustomMenuItemDto): String {
        return dbService.useTransaction {
            val e = dto.toEntity()
            val id = customMenuItemDao.insert(e)

            //            创建菜单项权限数据
            val allRoleId = roleService.findIdList()
            val roleIdSet = allRoleId.toMutableSet()
            val menuItemPermDtoList = mutableListOf<MenuItemPermDto>()
            dto.permList.forEach {
                if (roleIdSet.remove(it.roleId)) {
                    it.menuItemId = id
                    menuItemPermDtoList.add(it)
                }
            }
            roleIdSet.forEach {
                menuItemPermDtoList.add(MenuItemPermDto(it, id))
            }
            menuItemPermService.batchCreate(menuItemPermDtoList)

            id
        }
    }

    fun findAll(): MutableList<CustomMenuItem> {
        return customMenuItemDao.selectAll()
    }


    fun findById(id: String): CustomMenuItem? {
        return customMenuItemDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<CustomMenuItem> {
        return customMenuItemDao.selectByIds(ids)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomMenuItem> {
        return customMenuItemDao.pageSelect(null, pageSize, pageNum)
    }


}