package com.github.afezeria.hymn.script.web.controller

import com.github.afezeria.hymn.common.ann.ApiVersion
import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.platform.CacheService
import com.github.afezeria.hymn.core.module.service.BizObjectTriggerService
import com.github.afezeria.hymn.core.module.service.CustomApiService
import com.github.afezeria.hymn.core.module.service.CustomFunctionService
import com.github.afezeria.hymn.core.platform.script.ScriptType
import com.github.afezeria.hymn.script.platform.ScriptServiceImpl
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

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

    @Autowired
    private lateinit var scriptServiceImpl: ScriptServiceImpl

    @DeleteMapping("cache")
    @Function(AccountType.ADMIN)
    @ApiOperation(value = "清空脚本缓存", notes = "")
    @ResponseBody
    fun cleanLocalCache(
        @RequestParam("type") type: ScriptType,
        @RequestParam("key") key: String,
    ) {
        scriptServiceImpl.cleanLocalCache(type, key)
    }

    @PostMapping("debug")
    fun startDebug(
        @RequestParam("type") type: ScriptType,
        @RequestParam("api") api: String,
    ) {
        TODO()
    }

}