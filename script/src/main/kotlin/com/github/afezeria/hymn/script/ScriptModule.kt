package com.github.afezeria.hymn.script

import com.github.afezeria.hymn.common.module.AbstractModule
import com.github.afezeria.hymn.script.platform.ApiCache
import com.github.afezeria.hymn.script.platform.FunctionCache
import com.github.afezeria.hymn.script.platform.TriggerCache
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class ScriptModule : AbstractModule("script", {
    TriggerCache.cleanAllCache()
    ApiCache.cleanAllCache()
    FunctionCache.cleanAllCache()
})