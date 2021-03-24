package com.github.afezeria.hymn.script

import com.github.afezeria.hymn.common.constant.TriggerEvent
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.platform.CacheService
import com.github.afezeria.hymn.common.platform.dataservice.DataService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.common.util.toClass
import com.github.afezeria.hymn.core.conf.DataServiceConfiguration
import com.github.afezeria.hymn.core.module.service.BizObjectTriggerService
import com.github.afezeria.hymn.core.module.service.BusinessCodeRefService
import com.github.afezeria.hymn.core.module.service.CustomApiService
import com.github.afezeria.hymn.core.module.service.CustomFunctionService
import com.github.afezeria.hymn.core.platform.script.ScriptService
import com.github.afezeria.hymn.core.platform.script.ScriptType
import com.github.afezeria.hymn.core.platform.script.ScriptType.*
import org.graalvm.polyglot.Source
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.full.memberFunctions


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
    private lateinit var apiService: CustomApiService

    @Autowired
    private lateinit var functionService: CustomFunctionService

    @Autowired
    private lateinit var businessCodeRefService: BusinessCodeRefService

    @Autowired
    private lateinit var dataServiceConfiguration: DataServiceConfiguration

    private val pool = ContextWrapperPool


    companion object {
        private val triggerCache: MutableMap<String, SourceWithTime> = ConcurrentHashMap()
        private val interfaceCache: MutableMap<String, SourceWithTime> = ConcurrentHashMap()
        private val functionCache: MutableMap<String, SourceWithTime> = ConcurrentHashMap()
        private val codeReferenceByTrigger: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
        private val codeReferenceByInterface: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
        private val codeReferenceByFunction: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()

        private val dataServiceQueryMethod =
            DataService::class.memberFunctions.toList().filter { it.name.startsWith("query") }
                .map { it.name }
        private val dataServiceCrudMethod =
            DataService::class.memberFunctions.toList()
                .filter { it.name.matches(Regex("^(query|insert|update|delete)")) }
                .map { it.name }

    }

    private fun getTriggerSource(
        objectId: String,
        event: TriggerEvent
    ): Pair<List<SourceWithTime>, List<SourceWithTime>> {
        val triggerList = triggerService.findByBizObjectId(objectId)
            .filter { it.event == event.name }.sortedBy { it.ord }
        val functionIdSet = businessCodeRefService.findByTriggerIds(triggerList.map { it.id })
            .mapTo(mutableSetOf()) { it.refFunctionId!! }
        val offset = OffsetDateTime.now().offset
        val currentTimeMillis = System.currentTimeMillis()

//        获取待执行的触发器source
        val triggerSourceList = mutableListOf<SourceWithTime>()
        for (bizObjectTrigger in triggerList) {
            val sourceWithTime = triggerCache[bizObjectTrigger.api]
            if (sourceWithTime == null ||
                sourceWithTime.timestamp < bizObjectTrigger.modifyDate.toInstant(offset)
                    .toEpochMilli()
            ) {
                SourceWithTime(
                    bizObjectTrigger.api,
                    Source.create("js", bizObjectTrigger.code),
                    currentTimeMillis
                )
            } else {
                triggerSourceList.add(sourceWithTime)
            }
        }

        return triggerSourceList to getDependencyFunctionsource(functionIdSet)
    }

    private fun getApiSource(api: String): Pair<SourceWithTime, List<SourceWithTime>>? {
        val customInterface = apiService.findByApi(api) ?: return null
        val functionIdSet = businessCodeRefService.findByApiId(customInterface.id)
            .mapTo(mutableSetOf()) { it.byApiId!! }
        val offset = OffsetDateTime.now().offset
        val currentTimeMillis = System.currentTimeMillis()

        var sourceWithTime = interfaceCache[customInterface.api]
        if (sourceWithTime == null ||
            sourceWithTime.timestamp < customInterface.modifyDate.toInstant(offset)
                .toEpochMilli()
        ) {
            sourceWithTime = SourceWithTime(
                customInterface.api,
                Source.create("js", customInterface.code),
                currentTimeMillis
            )
        }

        return sourceWithTime to getDependencyFunctionsource(functionIdSet)
    }

    private fun getFunctionSource(api: String): Pair<SourceWithTime, List<SourceWithTime>>? {
        val function = functionService.findByApi(api) ?: return null
        val functionIdSet = businessCodeRefService.findByFunctionId(function.id)
            .mapTo(mutableSetOf()) { it.byApiId!! }
        val offset = OffsetDateTime.now().offset
        val currentTimeMillis = System.currentTimeMillis()

        var sourceWithTime = interfaceCache[function.api]
        if (sourceWithTime == null ||
            sourceWithTime.timestamp < function.modifyDate.toInstant(offset)
                .toEpochMilli()
        ) {
            sourceWithTime = SourceWithTime(
                function.api,
                Source.create("js", function.code),
                currentTimeMillis
            )
        }

        return sourceWithTime to getDependencyFunctionsource(functionIdSet)
    }

    private fun getDependencyFunctionsource(ids: Collection<String>): List<SourceWithTime> {
        val offset = OffsetDateTime.now().offset
        val currentTimeMillis = System.currentTimeMillis()
        val functionSourceList = mutableListOf<SourceWithTime>()
        val functions = functionService.findByIds(ids)
        for (function in functions) {
            val sourceWithTime = functionCache[function.api]
            if (sourceWithTime == null ||
                sourceWithTime.timestamp < function.modifyDate.toInstant(offset).toEpochMilli()
            ) {
                SourceWithTime(
                    function.api, Source.create("js", function.code), currentTimeMillis
                )
            } else {
                functionSourceList.add(sourceWithTime)
            }
        }
        return functionSourceList
    }

    override fun executeTrigger(
        dataService: DataService,
        event: TriggerEvent,
        objectId: String,
        old: Map<String, Any?>?,
        new: Map<String, Any?>?,
        tmpMap: MutableMap<String, Any?>,
        around: (String, () -> Unit) -> Unit
    ) {
        pool.use {
            val (triggers, functions) = getTriggerSource(objectId, event)
            var i = 0
            for (trigger in triggers) {
                if (i == 0) {
                    around.invoke(trigger.api) {
                        it.execute(
                            trigger, functions, dataService, old, new, tmpMap
                        )
                    }
                    i++
                } else {
                    around.invoke(trigger.api) {
                        it.execute(
                            trigger, emptyList(), dataService, old, new, tmpMap
                        )
                    }
                }
            }
        }
    }

    override fun executeInterface(
        dataService: DataService,
        api: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        pool.use {
            val (main, functions) = getApiSource(api) ?: return@use null
            it.execute(main, functions, dataService, request, response)
        }
    }

    override fun executeFunction(
        dataService: DataService,
        api: String,
        vararg params: Any?
    ): Any? {
        return pool.use {
            val (main, others) = getFunctionSource(api)
                ?: throw InnerException()
            it.execute(main, others, dataService, *params)
        }
    }

    override fun getScript(api: String): ((Array<Any?>) -> Any?)? {
        getFunctionSource(api) ?: return null
        return { params ->
            pool.use {
                val (main, others) = getFunctionSource(api)
                    ?: throw InnerException("")
                it.execute(main, others, *params)
            }
        }
    }

    override fun <T> compile(
        type: ScriptType,
        id: String?,
        api: String,
        lang: String,
        option: String?,
        code: String,
        txCallback: () -> T
    ): T {
        if (id == null) {
            compileNew(type, api, code, true)
        } else {
            compileUpdate(type, id, api, code, true)
        }
        return txCallback()
    }

    private fun checkApi(type: ScriptType, api: String) {
        when (type) {
            TRIGGER -> if (!api.startsWith("htri_")) throw CompileException("触发器api必须以 htri_ 开头")
            API -> if (!api.startsWith("hapi_")) throw CompileException("接口api必须以 hapi_ 开头")
            FUNCTION -> if (!api.startsWith("hfun_")) throw CompileException("自定义函数api必须以 hfun_ 开头")
        }
    }

    fun compileNew(
        type: ScriptType,
        api: String,
        code: String,
        updateDb: Boolean,
    ) {
        checkApi(type, api)
        val context = buildContext(debug = false, compile = true)
        val parse = context.getBindings("js").getMember("parse")
        val info = parse.execute("js", code).asString().toClass<ScriptInfo>()!!
        val warnings = mutableListOf<String>()
        info.apply {
            if (api != name) throw CompileException("函数名称和api不一致")
            if (type == TRIGGER) {
                if (params != listOf("dataService", "old_record", "new_record"))
                    throw CompileException("触发器参数必须为 dataService,old_record,new_record")
            }
            globalInvoke.filter { it.method.startsWith("hfun_") }
                .forEach {
                    functionService.findByApi(it.method)
                        ?: throw CompileException("line:${it.line}，自定义函数 ${it.method} 不存在")
                    if (it.arguments.isEmpty() || it.arguments[0] != "dataService") {
                        warnings.add("line:${it.line}，函数 ${it.method} 应该为 dataService")
                    }
                }
            memberInvoke.filter { it.obj == "dataService" }
                .forEach {
                    it.method

                }

        }


        TODO("Not yet implemented")

    }

    fun compileUpdate(
        type: ScriptType,
        id: String,
        api: String,
        code: String,
        updateDb: Boolean,
    ) {
        checkApi(type, api)
        val oldCode = when (type) {
            TRIGGER -> {
                triggerService.findById(id)?.code
                    ?: throw DataNotFoundException("trigger".msgById(id))
            }
            API -> {
                apiService.findById(id)?.code
                    ?: throw DataNotFoundException("interface".msgById(id))
            }
            FUNCTION -> {
                functionService.findById(id)?.code
                    ?: throw DataNotFoundException("function".msgById(id))
            }
        }
        val context = buildContext(debug = false, compile = true)
        val parse = context.getBindings("js").getMember("parse")
        val newInfo = parse.execute("js", code).asString().toClass<ScriptInfo>()!!
        val oldInfo = parse.execute("js", oldCode).asString().toClass<ScriptInfo>()!!

        TODO()
    }


    override fun execute(api: String, vararg params: Any?): Any? {
        return executeFunction(
            dataServiceConfiguration.createDataServiceImpl(true), api, *params
        )
    }
}