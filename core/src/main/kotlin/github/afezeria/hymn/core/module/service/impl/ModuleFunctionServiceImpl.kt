package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.ModuleFunction
import github.afezeria.hymn.core.module.dao.ModuleFunctionDao
import github.afezeria.hymn.core.module.dto.ModuleFunctionDto
import github.afezeria.hymn.core.module.service.ModuleFunctionService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class ModuleFunctionServiceImpl(
    private val moduleFunctionDao: ModuleFunctionDao,
) : ModuleFunctionService {
    override fun removeById(id: String): Int {
        moduleFunctionDao.selectById(id)
            ?: throw DataNotFoundException("ModuleFunction".msgById(id))
        val i = moduleFunctionDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: ModuleFunctionDto): Int {
        val e = moduleFunctionDao.selectById(id)
            ?: throw DataNotFoundException("ModuleFunction".msgById(id))
        dto.update(e)
        val i = moduleFunctionDao.update(e)
        return i
    }

    override fun create(dto: ModuleFunctionDto): String {
        val e = dto.toEntity()
        val id = moduleFunctionDao.insert(e)
        return id
    }

    override fun findAll(): List<ModuleFunction> {
        return moduleFunctionDao.selectAll()
    }


    override fun findById(id: String): ModuleFunction? {
        return moduleFunctionDao.selectById(id)
    }

    override fun findByModuleIdAndApi(
        moduleId: String,
        api: String,
    ): ModuleFunction? {
        return moduleFunctionDao.selectByModuleIdAndApi(moduleId,api,)
    }

    override fun findByModuleId(
        moduleId: String,
    ): List<ModuleFunction> {
        return moduleFunctionDao.selectByModuleId(moduleId,)
    }

    override fun findByApi(
        api: String,
    ): ModuleFunction? {
        return moduleFunctionDao.selectByApi(api,)
    }


}