package com.github.afezeria.hymn.script.web.controller

import com.github.afezeria.hymn.common.ann.ApiVersion
import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.platform.CacheService
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.core.module.service.BizObjectTriggerService
import com.github.afezeria.hymn.core.module.service.CustomApiService
import com.github.afezeria.hymn.core.module.service.CustomFunctionService
import com.github.afezeria.hymn.core.platform.script.ScriptType
import com.github.afezeria.hymn.script.platform.ContextWrapperPool
import com.github.afezeria.hymn.script.platform.ScriptServiceImpl
import com.github.afezeria.hymn.script.platform.SourceWithTime
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.graalvm.polyglot.Source
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

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "开启debug模式", notes = "")
    @PostMapping("debug")
    fun startDebug() {
        ContextWrapperPool.createDebugContext()
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "关闭debug模式", notes = "")
    @DeleteMapping("debug")
    fun stopDebug() {
        ContextWrapperPool.closeDebugContext()
    }

    @PostMapping("cache")
    @Function(AccountType.ADMIN)
    @ApiOperation(value = "编译临时脚本", notes = "")
    @ResponseBody
    fun compile(
        @RequestParam("type") type: ScriptType,
        @RequestParam("id") id: String,
        @RequestParam("baseFun", defaultValue = "false") baseFun: Boolean?,
        @RequestParam("api") api: String,
        @RequestParam("code") code: String,
    ): List<String> {
        val accountId = Session.getInstance().accountId
        val wrapperWithTimestamp = ContextWrapperPool.debugContextCache[accountId]
            ?: throw InnerException("未启用debug模式，临时脚本不会被处理")
        val compiler = scriptServiceImpl.getCompiler(
            type = type,
            id = id,
            baseFun = baseFun,
            api = api,
            code = code
        )
        if (compiler.errors.isEmpty()) {
//            编译没有错误时更新debug上下文中的脚本
            wrapperWithTimestamp.timestamp = System.currentTimeMillis()
            val name = when (type) {
                ScriptType.TRIGGER -> "trigger/$api.js"
                ScriptType.API -> "api/$api.js"
                ScriptType.FUNCTION -> "fun/$api.js"
            }
            wrapperWithTimestamp.wrapper.evaluated[api] = SourceWithTime(
                api = api,
                source = Source.newBuilder("js", code, name).build(),
//                临时脚本时间戳设置为最大值，避免被正常脚本覆盖
                timestamp = Long.MAX_VALUE,
                functionIds = emptyList()
            )
        }
        return compiler.errors
    }
}