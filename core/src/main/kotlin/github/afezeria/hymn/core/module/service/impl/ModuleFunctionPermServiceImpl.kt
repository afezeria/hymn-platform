package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import github.afezeria.hymn.core.module.dao.ModuleFunctionPermDao
import github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
import github.afezeria.hymn.core.module.service.ModuleFunctionPermService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class ModuleFunctionPermServiceImpl : ModuleFunctionPermService {

    @Autowired
    private lateinit var moduleFunctionPermDao: ModuleFunctionPermDao


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

    override fun findAll(): List<ModuleFunctionPerm> {
        return moduleFunctionPermDao.selectAll()
    }


    override fun findById(id: String): ModuleFunctionPerm? {
        return moduleFunctionPermDao.selectById(id)
    }

    override fun findByRoleIdAndModuleApiAndFunctionApi(
        roleId: String,
        moduleApi: String,
        functionApi: String,
    ): ModuleFunctionPerm? {
        return moduleFunctionPermDao.selectByRoleIdAndModuleApiAndFunctionApi(roleId,moduleApi,functionApi,)
    }

    override fun findByRoleId(
        roleId: String,
    ): List<ModuleFunctionPerm> {
        return moduleFunctionPermDao.selectByRoleId(roleId,)
    }


}