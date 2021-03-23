package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.entity.Module
import com.github.afezeria.hymn.core.module.entity.ModuleFunction

/**
 * @author afezeria
 */
interface ModuleService {
    fun getModuleByApi(api: String): Module?
    fun getAllModule(): MutableList<Module>
    fun getAllFunction(): MutableList<ModuleFunction>
    fun getFunctionByApi(api: String): ModuleFunction?
    fun getFunctionByModuleApi(moduleApi: String): MutableList<ModuleFunction>
}