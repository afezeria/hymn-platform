package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectFieldPermDao
import github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
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
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectFieldPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectFieldPerm".msgById(id))
        val i = bizObjectFieldPermDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectFieldPermDto): Int {
        val e = bizObjectFieldPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectFieldPerm".msgById(id))
        dto.update(e)
        val i = bizObjectFieldPermDao.update(e)
        return i
    }

    fun create(dto: BizObjectFieldPermDto): String {
        val e = dto.toEntity()
        val id = bizObjectFieldPermDao.insert(e)
        return id
    }

    fun findAll(): MutableList<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectAll()
    }


    fun findById(id: String): BizObjectFieldPerm? {
        return bizObjectFieldPermDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectByIds(ids)
    }


    fun findByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm? {
        return bizObjectFieldPermDao.selectByRoleIdAndFieldId(roleId, fieldId)
    }

    fun findByRoleIdAndFieldIdList(
        roleId: String,
        fieldIdList: Collection<String>
    ): MutableList<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.select({ (it.roleId eq roleId) and (it.fieldId inList fieldIdList) })
    }

    fun findByRoleId(
        roleId: String,
    ): MutableList<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectByRoleId(roleId)
    }

    fun findByFieldId(
        fieldId: String,
    ): MutableList<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.selectByFieldId(fieldId)
    }

    fun batchCreate(dtoList: List<BizObjectFieldPermDto>): Int {
        return bizObjectFieldPermDao.bulkInsert(dtoList.map { it.toEntity() })
    }

    fun batchSave(dtoList: List<BizObjectFieldPermDto>): Int {
        return bizObjectFieldPermDao.bulkInsertOrUpdate(dtoList.map { it.toEntity() })
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.pageSelect(null, pageSize, pageNum)
    }

    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): List<BizObjectFieldPerm> {
        return bizObjectFieldPermDao.select({ (it.roleId eq roleId) and (it.bizObjectId eq bizObjectId) })
    }


}