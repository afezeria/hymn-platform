package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.dao.BizObjectPermDao
import github.afezeria.hymn.core.module.dto.BizObjectPermDto
import github.afezeria.hymn.core.module.entity.BizObjectPerm
import github.afezeria.hymn.core.module.view.BizObjectPermListView
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

    fun findViewByBizObjectId(bizObjectId: String): List<BizObjectPermListView> {
        val viewRoleIdMap =
            bizObjectPermDao.selectView { it.bizObjectId eq bizObjectId }
                .map { it.roleId to it }.toMap()
        val objectName = viewRoleIdMap.values.first().bizObjectName
        return roleService.findAll()
            .map {
                viewRoleIdMap[it.id] ?: BizObjectPermListView(
                    roleId = it.id, bizObjectId = bizObjectId,
                    roleName = it.name,
                    bizObjectName = objectName,
                )
            }

    }

    fun findViewByRoleId(roleId: String): List<BizObjectPermListView> {
        val viewObjectIdMap =
            bizObjectPermDao.selectView { it.roleId eq roleId }
                .map { it.bizObjectId to it }.toMap()
        val roleName = viewObjectIdMap.values.first().roleName
        return bizObjectService.findAllActiveObject()
            .map {
                viewObjectIdMap[it.id] ?: BizObjectPermListView(
                    roleId = roleId, bizObjectId = it.id,
                    roleName = roleName,
                    bizObjectName = it.name,
                )
            }
    }

    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm? {
        return bizObjectPermDao.selectByRoleIdAndBizObjectId(roleId, bizObjectId)
    }

}