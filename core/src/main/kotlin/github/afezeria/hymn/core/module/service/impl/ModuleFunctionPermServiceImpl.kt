package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.ModuleFunctionPermDao
import github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import github.afezeria.hymn.core.module.service.ModuleFunctionPermService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ModuleFunctionPermServiceImpl : ModuleFunctionPermService {

    @Autowired
    private lateinit var moduleFunctionPermDao: ModuleFunctionPermDao

    @Autowired
    private lateinit var dbService: DataBaseService


    override fun removeById(id: String): Int {
        moduleFunctionPermDao.selectById(id)
            ?: throw DataNotFoundException("ModuleFunctionPerm".msgById(id))
        val i = moduleFunctionPermDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: ModuleFunctionPermDto): Int {
        val e = moduleFunctionPermDao.selectById(id)
            ?: throw DataNotFoundException("ModuleFunctionPerm".msgById(id))
        dto.update(e)
        val i = moduleFunctionPermDao.update(e)
        return i
    }

    override fun create(dto: ModuleFunctionPermDto): String {
        val e = dto.toEntity()
        val id = moduleFunctionPermDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<ModuleFunctionPerm> {
        return moduleFunctionPermDao.selectAll()
    }


    override fun findById(id: String): ModuleFunctionPerm? {
        return moduleFunctionPermDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<ModuleFunctionPerm> {
        return moduleFunctionPermDao.selectByIds(ids)
    }


    override fun findByRoleIdAndModuleApiAndFunctionApi(
        roleId: String,
        moduleApi: String,
        functionApi: String,
    ): ModuleFunctionPerm? {
        return moduleFunctionPermDao.selectByRoleIdAndModuleApiAndFunctionApi(
            roleId,
            moduleApi,
            functionApi,
        )
    }

    override fun findByRoleId(
        roleId: String,
    ): MutableList<ModuleFunctionPerm> {
        return moduleFunctionPermDao.selectByRoleId(roleId)
    }


    override fun batchCreate(dtoList: List<ModuleFunctionPermDto>): MutableList<Int> {
        return moduleFunctionPermDao.batchInsert(dtoList.map { it.toEntity() })
    }

    override fun batchSave(dtoList: List<ModuleFunctionPermDto>): MutableList<Int> {
        return moduleFunctionPermDao.batchInsertOrUpdate(dtoList.map { it.toEntity() })
    }
}