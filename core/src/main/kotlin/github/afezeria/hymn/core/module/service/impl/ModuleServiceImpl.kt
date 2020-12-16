package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.Module
import github.afezeria.hymn.core.module.dao.ModuleDao
import github.afezeria.hymn.core.module.dto.ModuleDto
import github.afezeria.hymn.core.module.service.ModuleService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class ModuleServiceImpl(
    private val moduleDao: ModuleDao,
) : ModuleService {
    override fun removeById(id: String): Int {
        moduleDao.selectById(id)
            ?: throw DataNotFoundException("Module".msgById(id))
        val i = moduleDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: ModuleDto): Int {
        val e = moduleDao.selectById(id)
            ?: throw DataNotFoundException("Module".msgById(id))
        dto.update(e)
        val i = moduleDao.update(e)
        return i
    }

    override fun create(dto: ModuleDto): String {
        val e = dto.toEntity()
        val id = moduleDao.insert(e)
        return id
    }

    override fun findAll(): List<Module> {
        return moduleDao.selectAll()
    }


    override fun findById(id: String): Module? {
        return moduleDao.selectById(id)
    }

    override fun findByApi(
        api: String,
    ): Module? {
        return moduleDao.selectByApi(api,)
    }


}