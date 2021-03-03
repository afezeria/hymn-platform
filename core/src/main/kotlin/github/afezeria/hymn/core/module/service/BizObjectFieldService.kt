package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectFieldDao
import github.afezeria.hymn.core.module.dto.BizObjectFieldDto
import github.afezeria.hymn.core.module.entity.BizObjectField
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectFieldService {

    @Autowired
    private lateinit var bizObjectFieldDao: BizObjectFieldDao

    @Autowired
    private lateinit var fieldPermService: BizObjectFieldPermService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectFieldDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectField".msgById(id))
        val i = bizObjectFieldDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectFieldDto): Int {
        return dbService.useTransaction {
            val e = bizObjectFieldDao.selectUpdatableField { it.id eq id }.firstOrNull()
                ?: throw DataNotFoundException("BizObjectField".msgById(id))
            dto.update(e)
            val i = bizObjectFieldDao.update(e)

            i
        }
    }

    fun create(dto: BizObjectFieldDto): String {
        return dbService.useTransaction {
            val e = dto.toEntity()
            val id = bizObjectFieldDao.insert(e)
            id
        }
    }

    fun findById(id: String): BizObjectField? {
        return bizObjectFieldDao.selectActiveField { it.id eq id }.firstOrNull()
    }

    fun findByIds(ids: List<String>): MutableList<BizObjectField> {
        return bizObjectFieldDao.selectActiveField { it.id inList ids }
    }

    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectField> {
        return bizObjectFieldDao.selectActiveField { it.bizObjectId eq bizObjectId }
    }

    fun findReferenceFieldByRefId(refObjectId: String): MutableList<BizObjectField> {
        return bizObjectFieldDao.selectByRefIdAndActiveTrue(refObjectId)
    }

    fun activateById(id: String): Int {
        val field = bizObjectFieldDao.selectCanModifyActiveStateFieldByIdAndActive(id, false)
            ?: return 0
        field.active = true
        return bizObjectFieldDao.update(field)
    }

    fun inactivateById(id: String): Int {
        val field = bizObjectFieldDao.selectCanModifyActiveStateFieldByIdAndActive(id, true)
            ?: return 0
        field.active = false
        return bizObjectFieldDao.update(field)
    }

    internal fun createDefaultField(objId: String, fieldName: String, autoRule: String?) {
        dbService.useTransaction {
            if (bizObjectFieldDao.selectByBizObjectId(objId).isNotEmpty()) {
                throw InnerException("对象 [id:${objId}] 不是新对象，无法创建默认字段")
            }
            bizObjectFieldDao.insert(
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
            bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "业务类型",
                    api = "type_id",
                    type = "reference",
                    refId = "09da56a7de514895aea5c596820d0ced",
                    refDeletePolicy = "no_action",
                    standardType = "type_id",
                    predefined = true,
                    sourceColumn = "type_id",
                )
            )
            bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "所有者",
                    api = "owner_id",
                    type = "reference",
                    refId = "bcf5f00c2e6c494ea2318912a639031a",
                    refDeletePolicy = "no_action",
                    standardType = "owner_id",
                    predefined = true,
                    sourceColumn = "owner_id",
                )
            )
            bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "创建人",
                    api = "create_by_id",
                    type = "reference",
                    refId = "bcf5f00c2e6c494ea2318912a639031a",
                    refDeletePolicy = "no_action",
                    standardType = "create_by_id",
                    predefined = true,
                    sourceColumn = "create_by_id",
                )
            )
            bizObjectFieldDao.insert(
                BizObjectField(
                    bizObjectId = objId,
                    name = "修改人",
                    api = "modify_by_id",
                    type = "reference",
                    refId = "bcf5f00c2e6c494ea2318912a639031a",
                    refDeletePolicy = "no_action",
                    standardType = "modify_by_id",
                    predefined = true,
                    sourceColumn = "modify_by_id",
                )
            )
            bizObjectFieldDao.insert(
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
            bizObjectFieldDao.insert(
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
            bizObjectFieldDao.insert(
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
        }
    }
}
