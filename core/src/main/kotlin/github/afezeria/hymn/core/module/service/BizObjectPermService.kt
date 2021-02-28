package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectPermDao
import github.afezeria.hymn.core.module.dto.BizObjectPermDto
import github.afezeria.hymn.core.module.entity.BizObjectPerm
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
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectPerm".msgById(id))
        val i = bizObjectPermDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectPermDto): Int {
        val e = bizObjectPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectPerm".msgById(id))
        dto.update(e)
        val i = bizObjectPermDao.update(e)
        return i
    }

    fun create(dto: BizObjectPermDto): String {
        val e = dto.toEntity()
        val id = bizObjectPermDao.insert(e)
        return id
    }

    fun batchCreate(dtoList: List<BizObjectPermDto>): Int {
        return bizObjectPermDao.bulkInsert(dtoList.map { it.toEntity() })
    }

    fun batchSave(dtoList: List<BizObjectPermDto>): Int {
        return bizObjectPermDao.bulkInsertOrUpdate(dtoList.map { it.toEntity() })
    }

    fun findAll(): MutableList<BizObjectPerm> {
        return bizObjectPermDao.selectAll()
    }


    fun findById(id: String): BizObjectPerm? {
        return bizObjectPermDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<BizObjectPerm> {
        return bizObjectPermDao.selectByIds(ids)
    }


    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm? {
        return bizObjectPermDao.selectByRoleIdAndBizObjectId(roleId, bizObjectId)
    }

    fun findByRoleId(
        roleId: String,
    ): MutableList<BizObjectPerm> {
        return bizObjectPermDao.selectByRoleId(roleId)
    }

    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectPerm> {
        return bizObjectPermDao.selectByBizObjectId(bizObjectId)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectPerm> {
        return bizObjectPermDao.pageSelect(null, pageSize, pageNum)
    }


}