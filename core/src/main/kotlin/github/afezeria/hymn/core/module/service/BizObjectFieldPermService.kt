package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dao.BizObjectFieldPermDao
import github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectFieldPermService {

    @Autowired
    private lateinit var bizObjectFieldPermDao: BizObjectFieldPermDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var fieldService: BizObjectFieldService

    @Autowired
    private lateinit var dbService: DatabaseService

    fun save(dtoList: List<BizObjectFieldPermDto>): Int {
        if (dtoList.isEmpty()) return 0
        return dbService.useTransaction {
            val inFieldIdSet = mutableSetOf<String>()
            val inRoleIdSet = mutableSetOf<String>()
            dtoList.forEach {
                inFieldIdSet.add(it.fieldId)
                inRoleIdSet.add(it.roleId)
            }
            val availableRoleIdSet =
                roleService.findByIds(inRoleIdSet).mapTo(mutableSetOf()) { it.id }
            val availableFieldIdSet =
                fieldService.findByIds(inFieldIdSet).mapTo(mutableSetOf()) { it.id }
            val entityList = dtoList.mapNotNull {
                if (availableFieldIdSet.contains(it.fieldId) &&
                    availableRoleIdSet.contains(it.roleId)
                ) {
                    it.toEntity()
                } else {
                    null
                }
            }
            bizObjectFieldPermDao.bulkInsertOrUpdate(
                entityList,
                *bizObjectFieldPermDao.table.run { arrayOf(roleId, fieldId) }
            )
        }
    }

    fun findDtoByFieldId(fieldId: String): List<BizObjectFieldPermDto> {
        return bizObjectFieldPermDao.selectDto { it, _ -> it.fieldId eq fieldId }
    }


    fun findDtoByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): List<BizObjectFieldPermDto> {
        return bizObjectFieldPermDao.selectDto { perm, field -> (perm.roleId eq roleId) and (field.bizObjectId eq bizObjectId) }
    }


    fun findByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm? {
        return bizObjectFieldPermDao.selectByRoleIdAndFieldId(roleId, fieldId)
    }
}