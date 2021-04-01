package com.github.afezeria.hymn.script.platform

import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Source
import org.graalvm.polyglot.Value

/**
 * @author afezeria
 */
class ContextWrapper(val debug: Boolean = false, lanIp: String = "") {
    val context: Context
    val bindings: Value
    val url: String

    init {
        val pair = buildContext(debug, false, lanIp)
        context = pair.first
        url = pair.second
        bindings = context.getBindings("js")
    }

    val evaluated = mutableMapOf<String, SourceWithTime>()

    companion object {
        private val checkSource = Source.create("js", "1")
    }

    fun execute(
        main: SourceWithTime,
        sources: List<SourceWithTime>,
        vararg params: Any?
    ): Any? {
//        检查依赖的自定义函数是否已求值且且代码为最新版本
        for (source in sources) {
            val contextSource = evaluated[source.api]
            if (contextSource == null || contextSource.timestamp < source.timestamp) {
                context.eval(source.source)
                evaluated[source.api] = source
            }
        }

//        检查主函数是否已求值且代码为最新版本
        val contextSource = evaluated[main.api]
        if (contextSource == null || contextSource.timestamp < main.timestamp) {
            context.eval(main.source)
            evaluated[main.api] = main
        }

        val scriptFunction = requireNotNull(bindings.getMember(main.api))
        val result = scriptFunction.execute(*params)
        return if (result.isNull) {
            null
        } else result
    }

    fun available(): Boolean {
        try {
            context.eval(checkSource)
        } catch (e: IllegalStateException) {
            return false
        }
        return true
    }

    fun destroy() {
        context.close(true)
    }
}