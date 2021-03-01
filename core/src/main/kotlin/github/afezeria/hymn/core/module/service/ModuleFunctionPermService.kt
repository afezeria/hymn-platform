package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.ModuleFunctionPermDao
import github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ModuleFunctionPermService {

    @Autowired
    private lateinit var moduleFunctionPermDao: ModuleFunctionPermDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        moduleFunctionPermDao.selectById(id)
            ?: throw DataNotFoundException("ModuleFunctionPerm".msgById(id))
        val i = moduleFunctionPermDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: ModuleFunctionPermDto): Int {
        val e = moduleFunctionPermDao.selectById(id)
            ?: throw DataNotFoundException("ModuleFunctionPerm".msgById(id))
        dto.update(e)
        val i = moduleFunctionPermDao.update(e)
        return i
    }

    fun create(dto: ModuleFunctionPermDto): String {
        val e = dto.toEntity()
        val id = moduleFunctionPermDao.insert(e)
        return id
    }

    fun findAll(): MutableList<ModuleFunctionPerm> {
        return moduleFunctionPermDao.selectAll()
    }


    fun findById(id: String): ModuleFunctionPerm? {
        return moduleFunctionPermDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<ModuleFunctionPerm> {
        return moduleFunctionPermDao.selectByIds(ids)
    }


    fun findByRoleIdAndModuleApiAndFunctionApi(
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

    fun findByRoleId(
        roleId: String,
    ): MutableList<ModuleFunctionPerm> {
        return moduleFunctionPermDao.selectByRoleId(roleId)
    }


    fun batchCreate(dtoList: List<ModuleFunctionPermDto>): Int {
        return moduleFunctionPermDao.bulkInsert(dtoList.map { it.toEntity() })
    }

    fun batchSave(dtoList: List<ModuleFunctionPermDto>): Int {
        return moduleFunctionPermDao.bulkInsertOrUpdate(dtoList.map { it.toEntity() })
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<ModuleFunctionPerm> {
        return moduleFunctionPermDao.pageSelect(null, pageSize, pageNum)
    }
}