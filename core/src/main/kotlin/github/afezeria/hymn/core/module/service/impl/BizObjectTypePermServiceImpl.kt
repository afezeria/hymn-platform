package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectTypePermDao
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import github.afezeria.hymn.core.module.service.BizObjectTypePermService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTypePermServiceImpl : BizObjectTypePermService {

    @Autowired
    private lateinit var bizObjectTypePermDao: BizObjectTypePermDao


    @Autowired
    private lateinit var dbService: DataBaseService


    override fun removeById(id: String): Int {
        bizObjectTypePermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypePerm".msgById(id))
        val i = bizObjectTypePermDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectTypePermDto): Int {
        val e = bizObjectTypePermDao.selectById(id)
            ?: throw DataNotFoundException("BizObjectTypePerm".msgById(id))
        dto.update(e)
        val i = bizObjectTypePermDao.update(e)
        return i
    }

    override fun create(dto: BizObjectTypePermDto): String {
        val e = dto.toEntity()
        val id = bizObjectTypePermDao.insert(e)


        return id
    }

    override fun findAll(): MutableList<BizObjectTypePerm> {
        return bizObjectTypePermDao.selectAll()
    }


    override fun findById(id: String): BizObjectTypePerm? {
        return bizObjectTypePermDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObjectTypePerm> {
        return bizObjectTypePermDao.selectByIds(ids)
    }


    override fun findByRoleIdAndTypeId(
        roleId: String,
        typeId: String,
    ): BizObjectTypePerm? {
        return bizObjectTypePermDao.selectByRoleIdAndTypeId(roleId, typeId)
    }

    override fun findByTypeId(
        typeId: String,
    ): MutableList<BizObjectTypePerm> {
        return bizObjectTypePermDao.selectByTypeId(typeId)
    }

    override fun batchCreate(dtoList: List<BizObjectTypePermDto>): MutableList<Int> {
        return bizObjectTypePermDao.batchInsert(dtoList.map { it.toEntity() })
    }

    override fun batchSave(dtoList: List<BizObjectTypePermDto>): MutableList<Int> {
        return bizObjectTypePermDao.batchInsertOrUpdate(dtoList.map { it.toEntity() })
    }


}