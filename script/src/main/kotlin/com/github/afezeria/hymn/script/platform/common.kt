package com.github.afezeria.hymn.script.platform

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.script.scriptutil.ScriptTools
import org.graalvm.polyglot.*

/**
 * @author afezeria
 */
fun getSource(file: String): Source? {
    val fileStr = requireNotNull(
        Thread.currentThread().contextClassLoader
            .getResourceAsStream(file)
    ).readAllBytes()
        .decodeToString()
    return Source.newBuilder("js", fileStr, "$file.js").build()
}

class ScriptBusinessException(msg: String) : BusinessException(msg)
class CompileException(msg: String) : InnerException(msg)

private val tools = ScriptTools()

val hostAccess =
    HostAccess.newBuilder()
        .allowPublicAccess(true)
        .denyAccess(System::class.java)
        .denyAccess(Class::class.java)
        .allowAllImplementations(true)
//        .allowImplementationsAnnotatedBy()
//        .allowImplementations(String::class.java)
        .allowAllClassImplementations(true)
        .allowArrayAccess(true)
        .allowListAccess(true)
//        .targetTypeMapping(Int::class.java, String::class.java,
//            { true }, { it?.toString() })
        .targetTypeMapping(List::class.java, List::class.java, {
            true
        }, { it })
        .targetTypeMapping(Map::class.java, Map::class.java,
            {
                val v = Value.asValue(it)
                v.hasMembers() && v.hasArrayElements().not()
            }, { it }
        )
        .build()

fun buildContext(debug: Boolean = false, compile: Boolean = false): Context {
    val builder = Context.newBuilder("js")
//        .allowAllAccess(true)
        .allowIO(false)
        .allowCreateProcess(false)
        .allowCreateThread(false)
        .allowEnvironmentAccess(EnvironmentAccess.NONE)
        .allowNativeAccess(false)
        .allowPolyglotAccess(PolyglotAccess.NONE)
        .allowHostClassLoading(false)
        .allowHostAccess(hostAccess)
//        nashor兼容模式
//        .option("js.nashorn-compat", "false")
//        禁用Regexp静态属性
        .option("js.regexp-static-result", "false")
//        指定js版本
//        .option("js.ecmascript-version","6")
//        允许支持条件的catch子句
        .option("js.syntax-extensions", "true")
//        禁止使用load函数
        .option("js.load", "false")
//        禁止js访问graal的Ployglot对象
        .option("js.polyglot-builtin", "false")
//        启用严格模式
//        .option("js.strict", "true")
        .allowHostClassLookup { true }
        .allowExperimentalOptions(true)

    if (debug) {
        builder
            .option("inspect", "0.0.0.0:9229")
//        不使用ssl，为true时需要指定密钥
            .option("inspect.Secure", "false")
//        程序初始化时挂起
            .option("inspect.Suspend", "false")
//        调试器连接前暂停，在连接后立刻开始执行
            .option("inspect.WaitAttached", "true")
//        .option("inspect.Internal", "true")
//        语言初始化时挂起
//        .option("inspect.Initialization", "true")
    }
    val context = builder.build()
    if (compile) {
        context.eval(getSource("acorn.js"))
        context.eval(getSource("acorn-walk.js"))
        context.eval(getSource("analysis.js"))
    }
    context.eval(getSource("hymn-extends.js"))
    context.eval(getSource("ext-string.js"))
    val bindings = context.getBindings("js")
    bindings.apply {
//        开启nashorn兼容模式后需要把这几个函数覆盖，避免从脚本引擎关闭虚拟机
        putMember("quit", null)
        putMember("exit", null)
        putMember("eval", null)
        putMember("tools", tools)
    }
    return context
}

class ScriptInfo(
    val name: String,
    val params: List<String>,
    val memberInvoke: List<MemberInvoke>,
    val globalInvoke: List<GlobalInvoke>,
)

class MemberInvoke(
    val obj: String,
    val method: String,
    val line: Int,
    val params: List<Parameter>
)

class GlobalInvoke(
    val method: String,
    val line: Int,
    val params: List<Parameter>
)

class Parameter(
    val type: ParameterType,
    val raw: String?,
)

enum class ParameterType {
    Literal, Identifier, Unknown
}
