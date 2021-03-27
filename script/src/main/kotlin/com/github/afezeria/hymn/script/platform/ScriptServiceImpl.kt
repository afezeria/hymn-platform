package com.github.afezeria.hymn.script.platform

import com.github.afezeria.hymn.common.constant.TriggerEvent
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.platform.CacheService
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.platform.dataservice.DataService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.common.util.toJson
import com.github.afezeria.hymn.core.conf.DataServiceConfiguration
import com.github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import com.github.afezeria.hymn.core.module.entity.BizObject
import com.github.afezeria.hymn.core.module.entity.BizObjectField
import com.github.afezeria.hymn.core.module.entity.CustomFunction
import com.github.afezeria.hymn.core.module.service.*
import com.github.afezeria.hymn.core.platform.script.ScriptService
import com.github.afezeria.hymn.core.platform.script.ScriptType
import com.github.afezeria.hymn.core.platform.script.ScriptType.*
import org.graalvm.polyglot.Source
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
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
    private lateinit var bizObjectService: BizObjectService

    @Autowired
    private lateinit var fieldService: BizObjectFieldService

    @Autowired
    private lateinit var apiService: CustomApiService

    @Autowired
    private lateinit var functionService: CustomFunctionService

    @Autowired
    private lateinit var businessCodeRefService: BusinessCodeRefService

    @Autowired
    private lateinit var dataServiceConfiguration: DataServiceConfiguration

    @Autowired
    private lateinit var databaseService: DatabaseService

    private val pool = ContextWrapperPool


    companion object {
        private val defaultOffset = OffsetDateTime.now().offset

        //objectId to triggerSourceList
        private val triggerCache: MutableMap<String, List<TriggerSourceWithTime>> =
            ConcurrentHashMap()

        //api to apiSource
        private val interfaceCache: MutableMap<String, SourceWithTime> = ConcurrentHashMap()

        //functionId to functionSource
        private val functionCache: MutableMap<String, SourceWithTime> = ConcurrentHashMap()

        //objectId to Lock
        private val triggerLockMap: MutableMap<String, Lock> = ConcurrentHashMap()

        private val codeReferenceByTrigger: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
        private val codeReferenceByInterface: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
        private val codeReferenceByFunction: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()

    }

    private fun getTriggerSource(
        objectId: String,
        event: TriggerEvent
    ): Pair<List<SourceWithTime>, List<SourceWithTime>> {
        val triggerSourceList = TriggerCache.getOrPut(objectId) {
            val triggerList = triggerService.findByBizObjectId(objectId)
            val functionMap =
                businessCodeRefService.findByTriggerIdsAndRefFunctionIdNotNull(triggerList.map { it.id })
                    .groupBy { requireNotNull(it.byTriggerId) }
            triggerList.map {
                TriggerSourceWithTime(
                    event = TriggerEvent.valueOf(it.event),
                    ord = it.ord,
                    api = it.api,
                    source = Source.newBuilder("js", it.code, "trigger/${it.api}.js").build(),
                    timestamp = it.modifyDate.toInstant(defaultOffset).toEpochMilli(),
                    functionIds = functionMap[it.id]?.map { requireNotNull(it.refFunctionId) }
                        ?: emptyList()
                )
            }
        }
        triggerSourceList.filter { it.event != event }.sortedBy { it.ord }

        TODO()
//        val triggerList = triggerService.findByBizObjectId(objectId)
//            .filter { it.event == event.name }.sortedBy { it.ord }
//        val functionIdSet = businessCodeRefService.findByTriggerIds(triggerList.map { it.id })
//            .mapTo(mutableSetOf()) { it.refFunctionId!! }
//        val offset = OffsetDateTime.now().offset
//        val currentTimeMillis = System.currentTimeMillis()
//
////        获取待执行的触发器source
//        val triggerSourceList = mutableListOf<SourceWithTime>()
//        for (bizObjectTrigger in triggerList) {
//            val sourceWithTime = triggerCache[bizObjectTrigger.api]
//            if (sourceWithTime == null ||
//                sourceWithTime.timestamp < bizObjectTrigger.modifyDate.toInstant(offset)
//                    .toEpochMilli()
//            ) {
//                SourceWithTime(
//                    bizObjectTrigger.api,
//                    Source.create("js", bizObjectTrigger.code),
//                    currentTimeMillis
//                )
//            } else {
//                triggerSourceList.add(sourceWithTime)
//            }
//        }
//
//        return triggerSourceList to getDependencyFunctionsource(functionIdSet)
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
                api = customInterface.api,
                source = Source.create("js", customInterface.code),
                timestamp = currentTimeMillis,
                functionIds = listOf(),
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
                api = function.api,
                source = Source.create("js", function.code),
                timestamp = currentTimeMillis,
                functionIds = listOf()
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
                    function.api,
                    Source.create("js", function.code),
                    currentTimeMillis,
                    functionIds = listOf()
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
        ContextWrapperPool.use {
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
        ContextWrapperPool.use {
            val (main, functions) = getApiSource(api) ?: return@use null
            it.execute(main, functions, dataService, request, response)
        }
    }

    override fun executeFunction(
        dataService: DataService,
        api: String,
        vararg params: Any?
    ): Any? {
        return ContextWrapperPool.use {
            val (main, others) = getFunctionSource(api)
                ?: throw InnerException()
            it.execute(main, others, dataService, *params)
        }
    }

    override fun getScript(api: String): ((Array<Any?>) -> Any?)? {
        getFunctionSource(api) ?: return null
        return { params ->
            ContextWrapperPool.use {
                val (main, others) = getFunctionSource(api)
                    ?: throw InnerException("")
                it.execute(main, others, *params)
            }
        }
    }

    /**
     * 清空集群缓存
     */
    fun cleanClusterCache(type: ScriptType, id: String?, objectId: String?) {
        if (id == null && objectId == null) return
        TODO()
    }

    fun cleanClusterAllCache() {
        TODO()
    }


    /**
     * 获取脚本中使用的函数数据
     */
    private fun getRefFunInfo(compiler: ScriptCompiler): List<CustomFunction> {
        //检查使用到的自定义函数是否都存在
        if (compiler.functionUsageList.isEmpty()) {
            return emptyList()
        }
        val functionList = functionService.findByApiList(compiler.functionUsageList.map { it.api })
        if (compiler.functionUsageList.size != functionList.size) {
            val functionApiSet = functionList.mapTo(mutableSetOf()) { it.api }
            compiler.functionUsageList.forEach {
                if (!functionApiSet.contains(it.api)) compiler.errors.add("line:${it.line}，自定义函数 ${it.api} 不存在")
            }
        }
        return functionList
    }

    /**
     * 获取脚本中使用的对象/字段数据
     */
    private fun getRefObjectAndFieldInfo(compiler: ScriptCompiler): Pair<Collection<BizObject>, List<BizObjectField>> {
        if (compiler.objectUsageList.isEmpty()) {
            return emptyList<BizObject>() to emptyList()
        }
//        检查使用到的业务对象
        val bizObjectMap =
            bizObjectService.findActiveObjectByApiList(compiler.objectUsageList.map { it.api }
                .toSet()).map { it.api to it }.toMap()
        compiler.objectUsageList.takeIf { it.size != bizObjectMap.size }?.forEach {
            if (!bizObjectMap.containsKey(it.api)) compiler.errors.add("line:${it.line}，业务对象 ${it.api} 不存在")
        }

        //检查使用到的字段
        val obj2Fields = mutableMapOf<String, MutableList<ScriptCompiler.FieldUsage>>()
        for (usage in compiler.objectUsageList) {
            val list = obj2Fields.getOrPut(usage.api) { mutableListOf() }
            for (field in usage.fields) {
                list.add(field)
            }
        }
        val usedBizObjectFieldList = mutableListOf<BizObjectField>()
        for ((objApi, fieldList) in obj2Fields) {
            bizObjectMap.get(objApi)?.let {
                val fieldApiMap =
                    fieldService.findByBizObjectId(it.id).map { it.api to it }.toMap()
                for (field in fieldList) {
                    if (fieldApiMap.containsKey(field.api)) {
                        usedBizObjectFieldList.add(fieldApiMap[field.api]!!)
                    } else {
                        compiler.errors.add("line:${field.line}，字段 [objectApi:${objApi},api:${field.api}] 不存在")
                    }
                }
            }
        }
        return bizObjectMap.values to usedBizObjectFieldList
    }

    private fun compileApi(
        id: String?,
        api: String,
        code: String,
    ): ScriptCompiler {
        val compiler = ScriptCompiler(API, api, code)
        if (id != null) {
            apiService.findById(id)?.code
                ?: throw DataNotFoundException("interface".msgById(id))
        }
        return compiler
    }

    private fun compileTrigger(
        id: String?,
        api: String,
        code: String,
    ): ScriptCompiler {
        val compiler = ScriptCompiler(TRIGGER, api, code)
        if (id != null) {
            triggerService.findById(id)?.code
                ?: throw DataNotFoundException("trigger".msgById(id))
        }
        return compiler
    }

    private fun compileFunction(
        id: String?,
        baseFun: Boolean,
        api: String,
        code: String,
    ): ScriptCompiler {
        val compiler = ScriptCompiler(FUNCTION, api, code)
        if (id != null) {
            val old = functionService.findById(id)?.code
                ?: throw DataNotFoundException("function".msgById(id))
            val oldCompiler = ScriptCompiler(FUNCTION, api, old)
            if (oldCompiler.api != compiler.api) {
                compiler.errors.add("不能修改函数api")
            }
            if (oldCompiler.info != null) {
                val nParamsNumber = requireNotNull(compiler.info).params.size
                val oParamsNumber = oldCompiler.info!!.params.size
                if (nParamsNumber != oParamsNumber) {
                    compiler.errors.add("不能修改参数个数")
                }
            }
        }
        if (baseFun && compiler.functionUsageList.isNotEmpty()) {
            compiler.errors.add("基础函数不能引用其他自定义函数")
        }
        return compiler
    }

    override fun compile(
        type: ScriptType,
        id: String?,
        objectId: String?,
        baseFun: Boolean?,
        api: String,
        lang: String,
        option: String?,
        code: String,
        txCallback: () -> String
    ): String {
        val compiler = when (type) {
            TRIGGER -> compileTrigger(id, api, code)
            API -> compileApi(id, api, code)
            FUNCTION -> compileFunction(id, requireNotNull(baseFun), api, code)
        }
        val functionList = getRefFunInfo(compiler)
        val (objectList, fieldList) = getRefObjectAndFieldInfo(compiler)
        if (compiler.errors.isNotEmpty()) {
            throw CompileException(compiler.errors.toJson())
        }

        val result = databaseService.useTransaction {
            cleanClusterCache(type, id, objectId)
            val res = txCallback()
            it.commit()
            val apiId = if (type == API) res else null
            val triggerId = if (type == TRIGGER) res else null
            val functionId = if (type == FUNCTION) res else null
            val businessCodeRefDtoList = mutableListOf<BusinessCodeRefDto>()
            for (customFunction in functionList) {
                businessCodeRefDtoList.add(
                    BusinessCodeRefDto(
                        byObjectId = null,
                        byTriggerId = triggerId,
                        byApiId = apiId,
                        byFunctionId = functionId,
                        refObjectId = null,
                        refFieldId = null,
                        refFunctionId = customFunction.id
                    )
                )
            }
            for (bizObject in objectList) {
                businessCodeRefDtoList.add(
                    BusinessCodeRefDto(
                        byObjectId = null,
                        byTriggerId = triggerId,
                        byApiId = apiId,
                        byFunctionId = functionId,
                        refObjectId = bizObject.id,
                        refFieldId = null,
                        refFunctionId = null
                    )
                )
            }
            for (field in fieldList) {
                businessCodeRefDtoList.add(
                    BusinessCodeRefDto(
                        byObjectId = null,
                        byTriggerId = triggerId,
                        byApiId = apiId,
                        byFunctionId = functionId,
                        refObjectId = null,
                        refFieldId = field.id,
                        refFunctionId = null
                    )
                )
            }
            businessCodeRefService.removeAutoGenData(triggerId, apiId, functionId)
            businessCodeRefService.save(businessCodeRefDtoList)
            cleanClusterCache(type, id, objectId)
            res
        }
        return result
    }

    override fun execute(api: String, vararg params: Any?): Any? {
        return executeFunction(
            dataServiceConfiguration.createDataServiceImpl(true), api, *params
        )
    }
}