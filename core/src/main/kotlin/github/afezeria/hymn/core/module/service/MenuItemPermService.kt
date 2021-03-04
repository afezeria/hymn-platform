package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dao.MenuItemPermDao
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
import org.ktorm.dsl.eq
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
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var dbService: DatabaseService

    fun findByItemId(menuItemId: String): MutableList<MenuItemPermDto> {
        return menuItemPermDao.selectDto { it.roleId eq menuItemId }
    }

    fun findByRoleId(roleId: String): MutableList<MenuItemPermDto> {
        return menuItemPermDao.selectDto { it.roleId eq roleId }
    }

    fun save(dtoList: List<MenuItemPermDto>): Int {
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