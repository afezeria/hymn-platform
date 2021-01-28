package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.Module
import github.afezeria.hymn.core.module.entity.ModuleFunction
import github.afezeria.hymn.core.module.service.ModuleService
import github.afezeria.hymn.core.module.table.ModuleFunctions
import github.afezeria.hymn.core.module.table.Modules
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ModuleServiceImpl : ModuleService {
    @Autowired
    private lateinit var dbService: DatabaseService

    val module = Modules()
    val function = ModuleFunctions()

    override fun getAllModule(): MutableList<Module> {
        return dbService.db().from(module)
            .select(module.columns)
            .mapTo(ArrayList()) { module.createEntity(it) }
    }

    override fun getAllFunction(): MutableList<ModuleFunction> {
        return dbService.db().from(function)
            .select(function.columns)
            .mapTo(ArrayList()) { function.createEntity(it) }
    }

    override fun getFunctionByModuleApi(moduleApi: String): MutableList<ModuleFunction> {
        return dbService.db().from(function)
            .select(function.columns)
            .where { function.moduleApi eq moduleApi }
            .mapTo(ArrayList()) { function.createEntity(it) }
    }
}