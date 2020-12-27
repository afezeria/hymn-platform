package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectPermDao
import github.afezeria.hymn.core.module.dto.BizObjectPermDto
import github.afezeria.hymn.core.module.entity.BizObjectPerm
import github.afezeria.hymn.core.module.service.BizObjectPermService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectPermServiceImpl : BizObjectPermService {

    @Autowired
    private lateinit var bizObjectPermDao: BizObjectPermDao

    @Autowired
    private lateinit var dbService: DataBaseService


    override fun removeById(id: String): Int {
        bizObjectPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectPerm".msgById(id))
        val i = bizObjectPermDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectPermDto): Int {
        val e = bizObjectPermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectPerm".msgById(id))
        dto.update(e)
        val i = bizObjectPermDao.update(e)
        return i
    }

    override fun create(dto: BizObjectPermDto): String {
        val e = dto.toEntity()
        val id = bizObjectPermDao.insert(e)
        return id
    }

    override fun batchCreate(dtoList: List<BizObjectPermDto>): MutableList<Int> {
        return bizObjectPermDao.batchInsert(dtoList.map { it.toEntity() })
    }

    override fun batchSave(dtoList: List<BizObjectPermDto>): MutableList<Int> {
        return bizObjectPermDao.batchInsertOrUpdate(dtoList.map { it.toEntity() })
    }

    override fun findAll(): MutableList<BizObjectPerm> {
        return bizObjectPermDao.selectAll()
    }


    override fun findById(id: String): BizObjectPerm? {
        return bizObjectPermDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObjectPerm> {
        return bizObjectPermDao.selectByIds(ids)
    }


    override fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm? {
        return bizObjectPermDao.selectByRoleIdAndBizObjectId(roleId, bizObjectId)
    }

    override fun findByRoleId(
        roleId: String,
    ): MutableList<BizObjectPerm> {
        return bizObjectPermDao.selectByRoleId(roleId)
    }

    override fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectPerm> {
        return bizObjectPermDao.selectByBizObjectId(bizObjectId)
    }


}