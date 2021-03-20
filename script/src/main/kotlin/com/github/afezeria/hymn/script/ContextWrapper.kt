package com.github.afezeria.hymn.script

import com.github.afezeria.hymn.common.platform.dataservice.DataService
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.Source
import org.graalvm.polyglot.Value

/**
 * @author afezeria
 */
class ContextWrapper {
    val context: Context
    val bindings: Value

    init {
        context = Context.newBuilder("js")
            .allowAllAccess(true)
            .allowHostAccess(
                hostAccess
            )
            .option("js.nashorn-compat", "true")
            .allowHostClassLookup { true }
            .allowExperimentalOptions(true)
            .build()
        bindings = context.getBindings("js")
    }

    val evaluated = mutableMapOf<String, SourceWithTime>()

    companion object {
        private val checkSource = Source.create("js", "1")
        private val hostAccess =
            HostAccess.newBuilder()
                .allowPublicAccess(true)
                .denyAccess(System::class.java)
                .denyAccess(Class::class.java)
                .allowAllImplementations(true)
                .allowArrayAccess(true)
                .allowListAccess(true)
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
    }

    fun execute(
        dataService: DataService,
        api: String,
        sources: List<SourceWithTime>,
        vararg params: Any?
    ): Any? {
        for (source in sources) {
            val contextSource = evaluated[source.api]
            if (contextSource == null || contextSource.timestamp < source.timestamp) {
                context.eval(source.source)
                evaluated[source.api] = source
            }
        }
        val scriptFunction = requireNotNull(bindings.getMember(api))
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