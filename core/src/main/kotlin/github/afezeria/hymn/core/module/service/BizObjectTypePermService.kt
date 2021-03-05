package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dao.BizObjectTypePermDao
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypePermService {

    @Autowired
    private lateinit var bizObjectTypePermDao: BizObjectTypePermDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var typeService: BizObjectTypeService

    @Autowired
    private lateinit var dbService: DatabaseService


    fun save(dtoList: List<BizObjectTypePermDto>): Int {
        if (dtoList.isEmpty()) return 0
        return dbService.useTransaction {
            val inTypeIdSet = mutableSetOf<String>()
            val inRoleIdSet = mutableSetOf<String>()
            dtoList.forEach {
                inTypeIdSet.add(it.typeId)
                inRoleIdSet.add(it.roleId)
            }
            val availableRoleIdSet =
                roleService.findByIds(inRoleIdSet).mapTo(mutableSetOf()) { it.id }
            val availableTypeIdSet =
                typeService.findByIds(inTypeIdSet).mapTo(mutableSetOf()) { it.id }
            val entityList = dtoList.mapNotNull {
                if (availableTypeIdSet.contains(it.typeId) &&
                    availableRoleIdSet.contains(it.roleId)
                ) {
                    it.toEntity()
                } else {
                    null
                }
            }
            bizObjectTypePermDao.bulkInsertOrUpdate(
                entityList,
                *bizObjectTypePermDao.table.run { arrayOf(roleId, typeId) }
            )
        }

    }

    fun findByTypeId(typeId: String): MutableList<BizObjectTypePermDto> {
        return bizObjectTypePermDao.selectDto { it, _ -> it.typeId eq typeId }
    }

    fun findDtoByRoleId(roleId: String): MutableList<BizObjectTypePermDto> {
        return bizObjectTypePermDao.selectDto { it, _ -> it.roleId eq roleId }
    }

    fun findDtoByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): MutableList<BizObjectTypePermDto> {
        return bizObjectTypePermDao.selectDto { perms, types ->
            (perms.roleId eq roleId) and (types.bizObjectId eq bizObjectId)
        }
    }

    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): MutableList<BizObjectTypePerm> {
        return bizObjectTypePermDao.findByRoleIdAndBizObjectId(roleId, bizObjectId)
    }


}