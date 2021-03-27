package com.github.afezeria.hymn.script.platform

import com.github.afezeria.hymn.common.platform.dataservice.DataService
import com.github.afezeria.hymn.common.util.toClass
import com.github.afezeria.hymn.core.platform.script.ScriptType
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.statement.select.SubSelect
import org.graalvm.polyglot.PolyglotException
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.valueParameters

/**
 * @author afezeria
 */
class ScriptCompiler(
    val type: ScriptType,
    val api: String,
    val code: String,
) {
    companion object {
        private val regex = Regex("^('.*'|\".*\")$")
        private val dataServiceDeclaredMemberFunctions = DataService::class.declaredMemberFunctions
            .map { it.name to it.valueParameters.map { it.name } }.toMap()
    }

    val functionUsageList = mutableListOf<FunctionUsage>()
    val objectUsageList = mutableListOf<ObjectUsage>()
    val errors = mutableListOf<String>()
    var info: ScriptInfo? = null

    init {
        compile()
    }


    class FunctionUsage(val line: Int, val api: String)
    class FieldUsage(val line: Int, val api: String)
    class ObjectUsage(
        val api: String,
        val line: Int,
        val fields: MutableList<FieldUsage> = mutableListOf()
    )

    private fun compile() {
        val context = buildContext(debug = false, compile = true)
        val parse = context.getBindings("js").getMember("parse")

        info = try {
            parse.execute(code).asString().toClass<ScriptInfo>()!!
        } catch (e: PolyglotException) {
            if (e.isGuestException) {
                errors.add(e.message ?: "")
            }
            return
        }
        info?.apply {
            if (api != name) errors.add("函数名称和api不一致")
            when (type) {
                ScriptType.TRIGGER -> {
                    if (!api.startsWith("htri_"))
                        errors.add("触发器api必须以 htri_ 开头")
                    if (params != listOf("dataService", "old_record", "new_record"))
                        errors.add("触发器参数必须为 dataService,old_record,new_record")
                }
                ScriptType.API -> {
                    if (!api.startsWith("hapi_")) errors.add("接口api必须以 hapi_ 开头")
                    if (params != listOf("dataService", "request", "response"))
                        errors.add("自定义接口参数必须为 dataService,request,response")
                }
                ScriptType.FUNCTION -> {
                    if (!api.startsWith("hfun_")) errors.add("自定义函数api必须以 hfun_ 开头")
                    if (params.first() != "dataService")
                        errors.add("自定义函数第一个参数必须为 dataService")
                }
            }
//            检查脚本中使用的自定义函数是否存在
            globalInvoke
                .onEach {
                    if (it.method.startsWith("htri_") || it.method.startsWith("hapi_"))
                        errors.add("line:${it.line}，不能调用 ${it.method}")
                }
                .filter { it.method.startsWith("hfun_") }
                .forEach {
                    functionUsageList.add(FunctionUsage(it.line, it.method))
                    if (it.params.isEmpty() ||
                        (it.params[0].type == ParameterType.Identifier && it.params[0].raw != "dataService")
                    ) {
                        errors.add("line:${it.line}，函数 ${it.method} 的第一个参数应该为 dataService")
                    }
                }
            memberInvoke.filter { it.obj == "dataService" }
                .forEach {
                    val list = dataServiceDeclaredMemberFunctions[it.method]
                    if (list == null) {
                        errors.add("line:${it.line}，dataService 不包含 ${it.method} 方法")
                        return@forEach
                    }
                    if (list.isEmpty()) return@forEach
                    //dataService的方法中名为objectApiName的参数都在第一个
                    if (list.contains("objectApiName")
                        && it.params[0].type == ParameterType.Literal
                        && it.params[0].raw != null
                        && regex.matches(it.params[0].raw!!)
                    ) {
                        val usage = ObjectUsage(it.params[0].raw!!.trim('\'', '"'), it.line)
                        objectUsageList.add(usage)
                        if (it.method.startsWith("query")
                            && !it.method.startsWith("queryById")
                            && it.params[1].type == ParameterType.Literal
                            && it.params[1].raw != null
                            && regex.matches(it.params[0].raw!!)
                        ) {
                            val expr = it.params[1].raw!!.trim('"', '\'')
                            CCJSqlParserUtil.parseCondExpression(expr)
                                .accept(object : ExpressionVisitorAdapter() {
                                    override fun visit(subSelect: SubSelect?) {
                                        errors.add("line:${it.line}，无效的where表达式，表达式中不能包含子查询")
                                    }

                                    override fun visit(column: Column?) {
                                        usage.fields.add(
                                            FieldUsage(
                                                it.line,
                                                requireNotNull(column).columnName
                                            )
                                        )
                                        super.visit(column)
                                    }
                                })
                        }
                    }
                }

        }
    }
}