package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectTypeDao
import github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import github.afezeria.hymn.core.module.entity.BizObjectType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypeService {

    @Autowired
    private lateinit var bizObjectTypeDao: BizObjectTypeDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var typePermService: BizObjectTypePermService

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectTypeDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectType".msgById(id))
        val i = bizObjectTypeDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectTypeDto): Int {
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

    fun create(dto: BizObjectTypeDto): String {
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

    fun findAll(): MutableList<BizObjectType> {
        return bizObjectTypeDao.selectAll()
    }


    fun findById(id: String): BizObjectType? {
        return bizObjectTypeDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<BizObjectType> {
        return bizObjectTypeDao.selectByIds(ids)
    }


    fun findByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectType? {
        return bizObjectTypeDao.selectByBizObjectIdAndName(bizObjectId, name)
    }

    fun findByBizObjectId(bizObjectId: String): List<BizObjectType> {
        TODO("Not yet implemented")
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectType> {
        return bizObjectTypeDao.pageSelect(null, pageSize, pageNum)
    }


}