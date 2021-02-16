package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectFieldDao
import github.afezeria.hymn.core.module.dto.BizObjectFieldDto
import github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import github.afezeria.hymn.core.module.entity.BizObjectField
import github.afezeria.hymn.core.module.service.BizObjectFieldPermService
import github.afezeria.hymn.core.module.service.BizObjectFieldService
import github.afezeria.hymn.core.module.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectFieldServiceImpl : BizObjectFieldService {

    @Autowired
    private lateinit var bizObjectFieldDao: BizObjectFieldDao

    @Autowired
    private lateinit var fieldPermService: BizObjectFieldPermService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        bizObjectFieldDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectField".msgById(id))
        val i = bizObjectFieldDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectFieldDto): Int {
        return dbService.useTransaction {
            val e = bizObjectFieldDao.selectById(id)
                ?: throw DataNotFoundException("BizObjectField".msgById(id))
            dto.update(e)
            val i = bizObjectFieldDao.update(e)

//        创建字段权限数据
            val roleIdSet = roleService.findIdList().toMutableSet()
            val fieldPermDtoList = dto.permList
                .filter { roleIdSet.contains(it.roleId) }
                .onEach { it.fieldId = id }
            fieldPermService.batchSave(fieldPermDtoList)

            i
        }
    }

    override fun create(dto: BizObjectFieldDto): String {
        return dbService.useTransaction {
            val e = dto.toEntity()
            val id = bizObjectFieldDao.insert(e)

//        创建字段权限数据
            val allRoleId = roleService.findIdList()
            val roleIdSet = allRoleId.toMutableSet()
            val fieldPermDtoList = mutableListOf<BizObjectFieldPermDto>()
            dto.permList.forEach {
                if (roleIdSet.remove(it.roleId)) {
                    it.fieldId = id
                    fieldPermDtoList.add(it)
                }
            }
            roleIdSet.forEach {
                fieldPermDtoList.add(BizObjectFieldPermDto(it, id))
            }
            fieldPermService.batchCreate(fieldPermDtoList)

            id
        }
    }

    override fun findAll(): MutableList<BizObjectField> {
        return bizObjectFieldDao.selectAll()
    }


    override fun findById(id: String): BizObjectField? {
        return bizObjectFieldDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObjectField> {
        return bizObjectFieldDao.selectByIds(ids)
    }


    override fun findByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectField? {
        return bizObjectFieldDao.selectByBizObjectIdAndApi(bizObjectId, api)
    }

    override fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectField> {
        return bizObjectFieldDao.selectByBizObjectId(bizObjectId)
    }

    override fun createDefaultField(objId: String, fieldName: String, autoRule: String?) {
        dbService.useTransaction {
            if (bizObjectFieldDao.selectByBizObjectId(objId).isNotEmpty()) {
                throw InnerException("对象 [id:${objId}] 不是新对象，无法创建默认字段")
            }
            val namefid = bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = fieldName,
                    api = "name",
                    type = if (autoRule != null) "text" else "auto",
                    maxLength = if (autoRule != null) 255 else null,
                    minLength = if (autoRule != null) 1 else null,
                    visibleRow = 1,
                    genRule = autoRule,
                    standardType = "name",
                    predefined = true,
                    sourceColumn = "name",
                )
            )
            val typefid = bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "业务类型",
                    api = "type_id",
                    type = "reference",
                    refId = "09da56a7de514895aea5c596820d0ced",
                    refDeletePolicy = "null",
                    standardType = "type_id",
                    predefined = true,
                    sourceColumn = "type_id",
                )
            )
            val ownerfid = bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "所有者",
                    api = "owner_id",
                    type = "reference",
                    refId = "bcf5f00c2e6c494ea2318912a639031a",
                    refDeletePolicy = "null",
                    standardType = "owner_id",
                    predefined = true,
                    sourceColumn = "owner_id",
                )
            )
            val cfid = bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "创建人",
                    api = "create_by_id",
                    type = "reference",
                    refId = "bcf5f00c2e6c494ea2318912a639031a",
                    refDeletePolicy = "null",
                    standardType = "create_by_id",
                    predefined = true,
                    sourceColumn = "create_by_id",
                )
            )
            val mfid = bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "修改人",
                    api = "modify_by_id",
                    type = "reference",
                    refId = "bcf5f00c2e6c494ea2318912a639031a",
                    refDeletePolicy = "null",
                    standardType = "modify_by_id",
                    predefined = true,
                    sourceColumn = "modify_by_id",
                )
            )
            val cdatefid = bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "创建时间",
                    api = "create_date",
                    type = "datetime",
                    standardType = "create_date",
                    predefined = true,
                    sourceColumn = "create_date",
                )
            )
            val mdatefid = bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "修改时间",
                    api = "modify_date",
                    type = "datetime",
                    standardType = "modify_date",
                    predefined = true,
                    sourceColumn = "modify_date",
                )
            )
            val lockfid = bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "锁定状态",
                    api = "lock_state",
                    type = "check_box",
                    standardType = "lock_state",
                    predefined = true,
                    sourceColumn = "lock_state"
                )
            )
//            创建默认的字段权限数据
            val roleIds = roleService.findIdList()
            val permList = ArrayList<BizObjectFieldPermDto>(roleIds.size * 8)
            roleIds.forEach {
                permList.add(
                    BizObjectFieldPermDto(
                        it, namefid, pRead = true,
                        pEdit = autoRule == null
                    )
                )
                permList.add(BizObjectFieldPermDto(it, typefid, true, pEdit = false))
                permList.add(BizObjectFieldPermDto(it, ownerfid, true, pEdit = false))
                permList.add(BizObjectFieldPermDto(it, cfid, true, pEdit = false))
                permList.add(BizObjectFieldPermDto(it, mfid, true, pEdit = false))
                permList.add(BizObjectFieldPermDto(it, cdatefid, true, pEdit = false))
                permList.add(BizObjectFieldPermDto(it, mdatefid, true, pEdit = false))
                permList.add(BizObjectFieldPermDto(it, lockfid, true, pEdit = false))
            }
            fieldPermService.batchCreate(permList)
        }
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectField> {
        return bizObjectFieldDao.pageSelect(null, pageSize, pageNum)
    }
}
