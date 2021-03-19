package com.github.afezeria.hymn.script

import com.github.afezeria.hymn.common.constant.TriggerEvent
import com.github.afezeria.hymn.common.platform.CacheService
import com.github.afezeria.hymn.common.platform.dataservice.DataService
import com.github.afezeria.hymn.common.platform.script.ScriptNotFoundException
import com.github.afezeria.hymn.core.conf.DataServiceConfiguration
import com.github.afezeria.hymn.core.module.service.BizObjectTriggerService
import com.github.afezeria.hymn.core.module.service.BusinessCodeRefService
import com.github.afezeria.hymn.core.module.service.CustomFunctionService
import com.github.afezeria.hymn.core.module.service.CustomInterfaceService
import com.github.afezeria.hymn.core.platform.script.CompileType
import com.github.afezeria.hymn.core.platform.script.ScriptService
import com.github.afezeria.hymn.core.platform.script.TriggerInfo
import org.graalvm.polyglot.Value
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author afezeria
 */
@Service
class ScriptServiceImpl : ScriptService {
    @Autowired
    private lateinit var cacheService: CacheService

    @Autowired
    private lateinit var triggerService: BizObjectTriggerService

    @Autowired
    private lateinit var interfaceService: CustomInterfaceService

    @Autowired
    private lateinit var functionService: CustomFunctionService

    @Autowired
    private lateinit var businessCodeRefService: BusinessCodeRefService

    @Autowired
    private lateinit var dataServiceConfiguration: DataServiceConfiguration

    companion object {
        private val triggerCache: MutableMap<String, Value> = ConcurrentHashMap()
        private val interfaceCache: MutableMap<String, Value> = ConcurrentHashMap()
        private val functionCache: MutableMap<String, Value> = ConcurrentHashMap()
        private val codeReferenceByTrigger: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
        private val codeReferenceByInterface: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
        private val codeReferenceByFunction: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
    }

    override fun executeTrigger(
        dataService: DataService,
        event: TriggerEvent,
        objectApiName: String,
        old: Map<String, Any?>?,
        new: Map<String, Any?>?,
        tmpMap: MutableMap<String, Any?>,
        around: (TriggerInfo, () -> Unit) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun executeInterface(
        dataService: DataService,
        api: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        TODO("Not yet implemented")
    }

    override fun executeFunction(
        dataService: DataService,
        api: String,
        params: MutableMap<String, Any?>
    ): Any? {
        TODO("Not yet implemented")
    }

    override fun getScript(api: String): ((Array<Any?>) -> Any?)? {

        TODO("Not yet implemented")
    }

    override fun <T> compile(
        type: CompileType,
        id: String?,
        lang: String,
        option: String?,
        code: String,
        txCallback: () -> T
    ): T {
        TODO("Not yet implemented")
    }

    private fun compileTrigger(
        id: String?,
        lang: String,
        option: String?,
        code: String,
    ): Value {
        TODO()
    }

    private fun compileInterface(
        id: String?,
        lang: String,
        option: String?,
        code: String,
    ): Value {
        TODO()
    }

    private fun compileFunction(
        id: String?,
        lang: String,
        option: String?,
        code: String,
    ): Value {
        TODO()
    }

    override fun execute(api: String, vararg params: Any?): Any? {
        return functionCache.getOrPut(api) {
            val function = functionService.findByApi(api) ?: throw ScriptNotFoundException(api)
            val value = function.let {
                compileFunction(it.id, it.lang, it.optionText, it.code)
            }
            value
        }.execute(*params)
    }
}