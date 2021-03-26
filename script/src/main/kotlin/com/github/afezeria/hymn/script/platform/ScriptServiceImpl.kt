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
import com.github.afezeria.hymn.core.module.entity.BizObjectField
import com.github.afezeria.hymn.core.module.service.*
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
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.valueParameters


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
        private val triggerCache: MutableMap<String, SourceWithTime> = ConcurrentHashMap()
        private val interfaceCache: MutableMap<String, SourceWithTime> = ConcurrentHashMap()
        private val functionCache: MutableMap<String, SourceWithTime> = ConcurrentHashMap()
        private val codeReferenceByTrigger: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
        private val codeReferenceByInterface: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
        private val codeReferenceByFunction: MutableMap<String, MutableList<String>> =
            ConcurrentHashMap()
        private val thread = ThreadLocal<Int>()

        private val dataServiceDeclaredMemberFunctions = DataService::class.declaredMemberFunctions
            .map { it.name to it.valueParameters.map { it.name } }.toMap()
        private val dataServiceQueryMethod =
            dataServiceDeclaredMemberFunctions
                .filter { it.key.contains("query") }
                .toMap()

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

    fun compileFunction(
        id: String?,
        baseFun: Boolean?,
        api: String,
        lang: String,
        option: String?,
        code: String,
    ) {
        val compiler = ScriptCompiler(FUNCTION, api, code)
        if (compiler.errors.isNotEmpty()) {
            throw CompileException(compiler.errors.toJson())
        }
        if (id != null) {
            val old = functionService.findById(id)?.code
                ?: throw DataNotFoundException("function".msgById(id))
            val oldCompiler = ScriptCompiler(FUNCTION, api, old)
            if (oldCompiler.api != compiler.api) {
                throw CompileException("不能修改函数api")
            }
            if (oldCompiler.info != null) {
                val nParamsNumber = requireNotNull(compiler.info).params.size
                val oParamsNumber = oldCompiler.info!!.params.size
                if (nParamsNumber != oParamsNumber) {
                    throw CompileException("不能修改参数个数")
                }
            }
        }

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
        val compiler = ScriptCompiler(type, api, code)
        if (compiler.errors.isNotEmpty()) {
            throw CompileException(compiler.errors.toJson())
        }
        if (id != null) {
            when (type) {
                TRIGGER -> {
                    triggerService.findById(id)?.code
                        ?: throw DataNotFoundException("trigger".msgById(id))
                }
                API -> {
                    apiService.findById(id)?.code
                        ?: throw DataNotFoundException("interface".msgById(id))
                }
                FUNCTION -> {
                    val old = functionService.findById(id)?.code
                        ?: throw DataNotFoundException("function".msgById(id))
                    val oldCompiler = ScriptCompiler(type, api, old)
                    if (oldCompiler.api != compiler.api) {
                        throw CompileException("不能修改函数api")
                    }
                    if (oldCompiler.info != null) {
                        val nParamsNumber = requireNotNull(compiler.info).params.size
                        val oParamsNumber = oldCompiler.info!!.params.size
                        if (nParamsNumber != oParamsNumber) {
                            throw CompileException("不能修改参数个数")
                        }
                    }
                }
            }
        }
        //检查使用到的自定义函数是否都存在
        val functionList = functionService.findByApiList(compiler.functionUsageList.map { it.api })
        if (compiler.functionUsageList.size != functionList.size) {
            val functionApiSet = functionList.mapTo(mutableSetOf()) { it.api }
            val errors =
                compiler.functionUsageList.mapNotNull { if (functionApiSet.contains(it.api)) null else "line:${it.line}，自定义函数 ${it.api} 不存在" }
            if (errors.isNotEmpty()) {
                throw CompileException(errors.toJson())
            }
        }

//        检查使用到的业务对象
        val bizObjectList =
            bizObjectService.findActiveObjectByApiList(compiler.objectUsageList.map { it.api }
                .toSet())
        if (compiler.objectUsageList.size != bizObjectList.size) {
            val objectApiSet = bizObjectList.mapTo(mutableSetOf()) { it.api }
            val errors =
                compiler.objectUsageList.mapNotNull { if (objectApiSet.contains(it.api)) null else "line:${it.line}，业务对象 ${it.api} 不存在" }
            if (errors.isNotEmpty()) {
                throw CompileException(errors.toJson())
            }
        }

        //检查使用到的字段
        val obj2Fields = mutableMapOf<String, MutableList<ScriptCompiler.FieldUsage>>()
        for (usage in compiler.objectUsageList) {
            val list = obj2Fields.getOrPut(usage.api) { mutableListOf() }
            for (field in usage.fields) {
                list.add(field)
            }
        }
        val errors = mutableListOf<String>()
        val usedBizObjectFieldList = mutableListOf<BizObjectField>()
        for ((objApi, fieldList) in obj2Fields) {
            val bizObject = requireNotNull(bizObjectList.find { it.api == objApi })
            val fieldApiMap =
                fieldService.findByBizObjectId(bizObject.id).map { it.api to it }.toMap()
            for (field in fieldList) {
                if (fieldApiMap.containsKey(field.api)) {
                    usedBizObjectFieldList.add(fieldApiMap[field.api]!!)
                } else {
                    errors.add("line:${field.line}，字段 [objectApi:${objApi},api:${field.api}] 不存在")
                }
            }
        }
        if (errors.isNotEmpty()) {
            throw CompileException(errors.toJson())
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
            for (bizObject in bizObjectList) {
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
            for (field in usedBizObjectFieldList) {
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