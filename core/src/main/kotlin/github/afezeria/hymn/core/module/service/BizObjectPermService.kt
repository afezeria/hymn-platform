package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dao.BizObjectPermDao
import github.afezeria.hymn.core.module.dto.BizObjectPermDto
import github.afezeria.hymn.core.module.entity.BizObjectPerm
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectPermService {

    @Autowired
    private lateinit var bizObjectPermDao: BizObjectPermDao

    @Autowired
    private lateinit var bizObjectService: BizObjectService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var dbService: DatabaseService

    fun save(dtoList: List<BizObjectPermDto>): Int {
        if (dtoList.isEmpty()) return 0
        return dbService.useTransaction {
            val availableRoleIdSet = roleService.findAll()
                .mapTo(mutableSetOf()) { it.id }
            val availableObjectIdSet =
                bizObjectService.findAllActiveObject().mapTo(mutableSetOf()) { it.id }
            val entityList = dtoList.mapNotNull {
                if (availableRoleIdSet.contains(it.roleId) &&
                    availableObjectIdSet.contains(it.bizObjectId)
                ) {
                    it.toEntity()
                } else {
                    null
                }
            }
            bizObjectPermDao.bulkInsertOrUpdate(
                entityList,
                *bizObjectPermDao.table.run { arrayOf(roleId, bizObjectId) }
            )
        }

    }

    fun findByBizObjectId(bizObjectId: String): MutableList<BizObjectPermDto> {
        return bizObjectPermDao.selectDto { it.bizObjectId eq bizObjectId }
    }

    fun findByRoleId(roleId: String): MutableList<BizObjectPermDto> {
        return bizObjectPermDao.selectDto { it.roleId eq roleId }
    }

    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm? {
        return bizObjectPermDao.selectByRoleIdAndBizObjectId(roleId, bizObjectId)
    }

}