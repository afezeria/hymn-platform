package com.github.afezeria.hymn.script.platform

import com.github.afezeria.hymn.common.ann.ApiVersion
import com.github.afezeria.hymn.common.constant.TriggerEvent
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.CacheService
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.platform.DbCache
import com.github.afezeria.hymn.common.platform.dataservice.DataService
import com.github.afezeria.hymn.common.util.delete
import com.github.afezeria.hymn.common.util.httpClient
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

    private val cache: DbCache by lazy {
        databaseService.getCache("module", 300)
    }

    companion object {
        private val defaultOffset = OffsetDateTime.now().offset
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
                        it.execute(trigger, functions, dataService, old, new, tmpMap)
                    }
                    i++
                } else {
                    around.invoke(trigger.api) {
                        it.execute(
                            trigger,
                            //第一次执行已经检查了所有基础自定义函数，后续执行不在检查
                            emptyList(),
                            dataService, old, new, tmpMap
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
            val (main, functions) = getApiSource(api)
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
            it.execute(main, others, dataService, *params)
        }
    }

    override fun execute(api: String, vararg params: Any?): Any? {
        return executeFunction(
            dataServiceConfiguration.createDataServiceImpl(true), api, *params
        )
    }

    override fun getScript(api: String): ((Array<Any?>) -> Any?)? {
        val customFunction = functionService.findByApi(api) ?: return null
        //不能单独执行基础函数
        if (customFunction.baseFun) return null
        return { params ->
            ContextWrapperPool.use {
                val (main, others) = getFunctionSource(customFunction.id)
                it.execute(main, others, *params)
            }
        }
    }

    private fun getTriggerSource(
        objectId: String,
        event: TriggerEvent
    ): Pair<List<SourceWithTime>, List<SourceWithTime>> {
        val sourceList = mutableListOf<TriggerSourceWithTime>()
        val functionIdSet = mutableSetOf<String>()
        TriggerCache.getOrPut(objectId) {
            val triggerList = triggerService.findByBizObjectId(objectId)
            val functionMap =
                businessCodeRefService.findPairByTriggerIds(triggerList.map { it.id })
                    .groupBy { it.first }
            triggerList.map {
                TriggerSourceWithTime(
                    event = TriggerEvent.valueOf(it.event),
                    ord = it.ord,
                    api = it.api,
                    source = Source.newBuilder("js", it.code, "trigger/${it.api}.js").build(),
                    timestamp = it.modifyDate.toInstant(defaultOffset).toEpochMilli(),
                    functionIds = functionMap[it.id]?.map { requireNotNull(it.second) }
                        ?: emptyList()
                )
            }
        }.forEach {
            if (it.event == event) {
                sourceList.add(it)
                functionIdSet.addAll(it.functionIds)
            }
        }
        sourceList.sortBy { it.ord }

        return sourceList to getRefFunctionSource(functionIdSet)
    }

    private fun getApiSource(api: String): Pair<SourceWithTime, List<SourceWithTime>> {
        val apiSource = ApiCache.getOrPut(api) {
            val customApi = apiService.findByApi(api) ?: throw ApiNotFoundException(api)
            val functionIds =
                businessCodeRefService.findBaseFunctionIdsByApiId(customApi.id)

            SourceWithTime(
                api = api,
                source = Source.newBuilder("js", customApi.code, "api/${api}.js").build(),
                timestamp = customApi.modifyDate.toInstant(defaultOffset).toEpochMilli(),
                functionIds = functionIds,
            )
        }
        return apiSource to getRefFunctionSource(apiSource.functionIds)
    }

    /**
     * 返回非基础函数
     * @throws [CustomFunctionNotFoundException] 如果函数不存在或函数不是基础函数
     */
    private fun getFunctionSource(id: String): Pair<SourceWithTime, List<SourceWithTime>> {
        val functionSource = FunctionCache.getOrPut(id) {
            val customFunction =
                functionService.findById(id) ?: throw CustomFunctionNotFoundException(id)
            if (customFunction.baseFun) throw CustomFunctionNotFoundException(id)
            val functionIds =
                businessCodeRefService.findBaseFunctionIdsByFunctionId(customFunction.id)

            SourceWithTime(
                api = customFunction.api,
                source = Source.newBuilder(
                    "js",
                    customFunction.code,
                    "fun/${customFunction.api}.js"
                ).build(),
                timestamp = customFunction.modifyDate.toInstant(defaultOffset).toEpochMilli(),
                functionIds = functionIds,
            )
        }
        return functionSource to getRefFunctionSource(functionSource.functionIds)

    }

    private fun getRefFunctionSource(ids: Collection<String>): List<SourceWithTime> {
        return ids.map { id ->
            FunctionCache.getOrPut(id) {
                val customApi =
                    functionService.findBaseFunctionById(id)
                        ?: throw CustomFunctionNotFoundException(id)
                SourceWithTime(
                    api = customApi.api,
                    source = Source.newBuilder("js", customApi.code, "fun/${customApi.api}.js")
                        .build(),
                    timestamp = customApi.modifyDate.toInstant(defaultOffset).toEpochMilli(),
                    functionIds = emptyList(),
                )
            }
        }
    }

    /**
     * 清空集群缓存
     */
    fun cleanClusterCache(type: ScriptType, id: String?, objectId: String?, api: String?) {
        val key = requireNotNull(
            when (type) {
                TRIGGER -> objectId
                API -> api
                FUNCTION -> id
            }
        )
        cleanLocalCache(type, key)
        val urls = cache.get("script:%")
        urls.forEach {
            httpClient.delete(
                "http://${it.second}/module/script/api/v${ApiVersion.lowest}/script/cache",
                params = mapOf(
                    "type" to type.name,
                    "key" to key
                )
            )
        }
    }

    fun cleanLocalCache(type: ScriptType, key: String) {
        when (type) {
            TRIGGER -> TriggerCache.clean(key)
            API -> ApiCache.clean(key)
            FUNCTION -> FunctionCache.clean(key)
        }
    }

    fun getCompiler(
        type: ScriptType,
        id: String?,
        baseFun: Boolean?,
        api: String,
        code: String,
    ): ScriptCompiler {
        val compiler = when (type) {
            TRIGGER -> compileTrigger(id, api, code)
            API -> compileApi(id, api, code)
            FUNCTION -> compileFunction(id, requireNotNull(baseFun), api, code)
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
//        val compiler = when (type) {
//            TRIGGER -> compileTrigger(id, api, code)
//            API -> compileApi(id, api, code)
//            FUNCTION -> compileFunction(id, requireNotNull(baseFun), api, code)
//        }
//        val functionList = getRefFunInfo(compiler)
//        val (objectList, fieldList) = getRefObjectAndFieldInfo(compiler)
        val compiler = getCompiler(type, id, baseFun, api, code)
        if (compiler.errors.isNotEmpty()) {
            throw CompileException(compiler.errors.toJson())
        }

        val result = databaseService.useTransaction {
            cleanClusterCache(type, id, objectId, api)
            val res = txCallback()
            it.commit()
            val apiId = if (type == API) res else null
            val triggerId = if (type == TRIGGER) res else null
            val functionId = if (type == FUNCTION) res else null
            val businessCodeRefDtoList = mutableListOf<BusinessCodeRefDto>()
            for (customFunction in compiler.customFunctionList) {
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
            for (bizObject in compiler.bizObjectList) {
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
            for (field in compiler.bizObjectFieldList) {
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
            cleanClusterCache(type, id, objectId, api)
            res
        }
        return result
    }

    private fun compileApi(
        id: String?,
        api: String,
        code: String,
    ): ScriptCompiler {
        val compiler = ScriptCompiler(API, api, code)
        if (id != null) {
            val oldApi = apiService.findById(id)
                ?: throw DataNotFoundException("interface".msgById(id))
            if (oldApi.api != api) {
                compiler.errors.add("不能修改api")
            }
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
            val oldTrigger = triggerService.findById(id)
                ?: throw DataNotFoundException("trigger".msgById(id))
            if (oldTrigger.api != api) {
                compiler.errors.add("不能修改api")
            }
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
        compiler.customFunctionList = functionList
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
        compiler.bizObjectList = bizObjectMap.values.toList()
        compiler.bizObjectFieldList = usedBizObjectFieldList
        return bizObjectMap.values to usedBizObjectFieldList
    }
}