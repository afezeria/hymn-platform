package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.Module
import github.afezeria.hymn.core.module.entity.ModuleFunction

/**
 * @author afezeria
 */
internal interface ModuleService {
    fun getAllModule(): MutableList<Module>
    fun getAllFunction(): MutableList<ModuleFunction>
    fun getFunctionByModuleApi(moduleApi: String): MutableList<ModuleFunction>
}