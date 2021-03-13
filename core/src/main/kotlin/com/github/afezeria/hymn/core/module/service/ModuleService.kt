package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.Module
import com.github.afezeria.hymn.core.module.entity.ModuleFunction
import com.github.afezeria.hymn.core.module.table.CoreModuleFunctions
import com.github.afezeria.hymn.core.module.table.CoreModules
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ModuleService {
    @Autowired
    private lateinit var dbService: DatabaseService

    val module = CoreModules()
    val function = CoreModuleFunctions()

    fun getModuleByApi(api: String): Module? {
        return dbService.db().from(module)
            .select(module.columns)
            .where { module.api eq api }
            .mapTo(ArrayList()) { module.createEntity(it) }
            .firstOrNull()
    }

    fun getAllModule(): MutableList<Module> {
        return dbService.db().from(module)
            .select(module.columns)
            .mapTo(ArrayList()) { module.createEntity(it) }
    }

    fun getAllFunction(): MutableList<ModuleFunction> {
        return dbService.db().from(function)
            .select(function.columns)
            .mapTo(ArrayList()) { function.createEntity(it) }
    }

    fun getFunctionByApi(api: String): ModuleFunction? {
        return dbService.db().from(function)
            .select(function.columns)
            .where { function.api eq api }
            .map { function.createEntity(it) }
            .firstOrNull()
    }

    fun getFunctionByModuleApi(moduleApi: String): MutableList<ModuleFunction> {
        return dbService.db().from(function)
            .select(function.columns)
            .where { function.moduleApi eq moduleApi }
            .mapTo(ArrayList()) { function.createEntity(it) }
    }
}