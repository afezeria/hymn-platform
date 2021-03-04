package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dao.ButtonPermDao
import github.afezeria.hymn.core.module.dto.ButtonPermDto
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ButtonPermService {

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

    fun findByButtonId(buttonId: String): MutableList<ButtonPermDto> {
        return buttonPermDao.selectDto { it, _ -> it.buttonId eq buttonId }
    }

    fun findByRoleId(roleId: String): MutableList<ButtonPermDto> {
        return buttonPermDao.selectDto { it, _ -> it.roleId eq roleId }
    }

    fun save(dtoList: List<ButtonPermDto>): Int {
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