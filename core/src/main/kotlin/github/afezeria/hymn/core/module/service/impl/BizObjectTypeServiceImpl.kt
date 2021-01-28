package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectTypeDao
import github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import github.afezeria.hymn.core.module.entity.BizObjectType
import github.afezeria.hymn.core.module.service.BizObjectTypePermService
import github.afezeria.hymn.core.module.service.BizObjectTypeService
import github.afezeria.hymn.core.module.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypeServiceImpl : BizObjectTypeService {

    @Autowired
    private lateinit var bizObjectTypeDao: BizObjectTypeDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var typePermService: BizObjectTypePermService

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        bizObjectTypeDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectType".msgById(id))
        val i = bizObjectTypeDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectTypeDto): Int {
        return dbService.useTransaction {
            val e = bizObjectTypeDao.selectById(id)
                ?: throw DataNotFoundException("BizObjectType".msgById(id))
            dto.update(e)
            val i = bizObjectTypeDao.update(e)
            //            更新类型权限数据
            val roleIdSet = roleService.findIdList().toSet()
            val typePermDtoList = dto.permList
                .filter { roleIdSet.contains(it.roleId) }
                .onEach { it.typeId = id }
            typePermService.batchSave(typePermDtoList)

            i
        }
    }

    override fun create(dto: BizObjectTypeDto): String {
        return dbService.useTransaction {
            val e = dto.toEntity()
            val id = bizObjectTypeDao.insert(e)

            //            创建类型权限数据
            val allRoleId = roleService.findIdList()
            val roleIdSet = allRoleId.toMutableSet()
            val typePermDtoList = mutableListOf<BizObjectTypePermDto>()
            dto.permList.forEach {
                if (roleIdSet.remove(it.roleId)) {
                    it.typeId = id
                    typePermDtoList.add(it)
                }
            }
            roleIdSet.forEach {
                typePermDtoList.add(BizObjectTypePermDto(it, id))
            }
            typePermService.batchCreate(typePermDtoList)

            id
        }
    }

    override fun findAll(): MutableList<BizObjectType> {
        return bizObjectTypeDao.selectAll()
    }


    override fun findById(id: String): BizObjectType? {
        return bizObjectTypeDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObjectType> {
        return bizObjectTypeDao.selectByIds(ids)
    }


    override fun findByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectType? {
        return bizObjectTypeDao.selectByBizObjectIdAndName(bizObjectId, name)
    }


}