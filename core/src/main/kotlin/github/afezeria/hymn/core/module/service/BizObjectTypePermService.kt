package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectTypePermDao
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
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
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectTypePermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypePerm".msgById(id))
        val i = bizObjectTypePermDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectTypePermDto): Int {
        val e = bizObjectTypePermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypePerm".msgById(id))
        dto.update(e)
        val i = bizObjectTypePermDao.update(e)
        return i
    }

    fun create(dto: BizObjectTypePermDto): String {
        val e = dto.toEntity()
        val id = bizObjectTypePermDao.insert(e)


        return id
    }

    fun findAll(): MutableList<BizObjectTypePerm> {
        return bizObjectTypePermDao.selectAll()
    }


    fun findById(id: String): BizObjectTypePerm? {
        return bizObjectTypePermDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<BizObjectTypePerm> {
        return bizObjectTypePermDao.selectByIds(ids)
    }


    fun findByRoleIdAndTypeId(
        roleId: String,
        typeId: String,
    ): BizObjectTypePerm? {
        return bizObjectTypePermDao.selectByRoleIdAndTypeId(roleId, typeId)
    }

    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): MutableList<BizObjectTypePerm> {
        return bizObjectTypePermDao.findByRoleIdAndBizObjectId(roleId, bizObjectId)
    }

    fun findByTypeId(
        typeId: String,
    ): MutableList<BizObjectTypePerm> {
        return bizObjectTypePermDao.selectByTypeId(typeId)
    }

    fun batchCreate(dtoList: List<BizObjectTypePermDto>): Int {
        return bizObjectTypePermDao.bulkInsert(dtoList.map { it.toEntity() })
    }

    fun batchSave(dtoList: List<BizObjectTypePermDto>): Int {
        return bizObjectTypePermDao.bulkInsertOrUpdate(dtoList.map { it.toEntity() })
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectTypePerm> {
        return bizObjectTypePermDao.pageSelect(null, pageSize, pageNum)
    }


}