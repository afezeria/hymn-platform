package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.CustomMenuItemDao
import github.afezeria.hymn.core.module.dto.CustomMenuItemDto
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
import github.afezeria.hymn.core.module.entity.CustomMenuItem
import github.afezeria.hymn.core.module.service.CustomMenuItemService
import github.afezeria.hymn.core.module.service.MenuItemPermService
import github.afezeria.hymn.core.module.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomMenuItemServiceImpl : CustomMenuItemService {

    @Autowired
    private lateinit var customMenuItemDao: CustomMenuItemDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var menuItemPermService: MenuItemPermService

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        customMenuItemDao.selectById(id)
            ?: throw DataNotFoundException("CustomMenuItem".msgById(id))
        val i = customMenuItemDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CustomMenuItemDto): Int {
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

    override fun create(dto: CustomMenuItemDto): String {
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

    override fun findAll(): MutableList<CustomMenuItem> {
        return customMenuItemDao.selectAll()
    }


    override fun findById(id: String): CustomMenuItem? {
        return customMenuItemDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<CustomMenuItem> {
        return customMenuItemDao.selectByIds(ids)
    }


}