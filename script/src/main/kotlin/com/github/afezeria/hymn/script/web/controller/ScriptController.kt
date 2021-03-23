package com.github.afezeria.hymn.script.web.controller

import com.github.afezeria.hymn.common.ann.ApiVersion
import com.github.afezeria.hymn.common.platform.CacheService
import com.github.afezeria.hymn.core.module.service.BizObjectTriggerService
import com.github.afezeria.hymn.core.module.service.CustomApiService
import com.github.afezeria.hymn.core.module.service.CustomFunctionService
import com.github.afezeria.hymn.script.ScriptType
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/script/api/{version}/script")
@Api(tags = ["ScriptController"], description = "脚本接口")
class ScriptController {
    @Autowired
    private lateinit var cacheService: CacheService

    @Autowired
    private lateinit var triggerService: BizObjectTriggerService

    @Autowired
    private lateinit var apiService: CustomApiService

    @Autowired
    private lateinit var functionService: CustomFunctionService

    fun cleanCache() {

    }

    @PostMapping("clean-cache")
    fun cleanLocalCache() {
        TODO()
    }

    @PostMapping("debug")
    fun startDebug(
        @RequestParam("type") type: ScriptType,
        @RequestParam("api") api: String,
    ) {
        TODO()
    }

}