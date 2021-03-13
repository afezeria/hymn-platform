package com.github.afezeria.hymn.script

import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.Value
import org.intellij.lang.annotations.Language

/**
 * @author afezeria
 */

fun main() {
    val polyglot = Context.newBuilder()
        .allowAllAccess(true)
        .allowHostAccess(
            HostAccess.newBuilder()
                .allowPublicAccess(true)
                .denyAccess(System::class.java)
                .denyAccess(Class::class.java)
                .allowAllImplementations(true)
                .allowArrayAccess(true)
                .allowListAccess(true)
                .targetTypeMapping(List::class.java, List::class.java, {
                    println("convert to list")
                    true
                }, { it })
                .targetTypeMapping(Map::class.java, Map::class.java,
                    {
                        println("convert to map")
                        val v = Value.asValue(it)
                        v.hasMembers() && v.hasArrayElements().not()
                    }, { it }
                )
                .build()
        )
        .option("js.nashorn-compat", "true")
        .option("inspect", "0.0.0.0:9229")
        .option("inspect.Secure", "false")
        .option("inspect.Suspend", "false")
        .option("inspect.WaitAttached", "false")
        .option("inspect.Internal", "true")
        .option("inspect.Initialization", "true")
        .allowHostClassLookup { true }
        .allowExperimentalOptions(true)
        .build()

    @Language("JavaScript")
    val js = """

function tt(a:String){

}
        function test(a,b) {
        abc.bb([1,2]);
        abc.bb({1:2});
//        abc.aa({3:24,"bb":[32]})
          console.log(a);
          console.log(b);
          return [1,2]
        }
    """

    val jsbind = polyglot.getBindings("js")
    jsbind.putMember("abc", Abc())
    polyglot.eval("js", js)
//    (1..5).forEach {
//        Thread.sleep(1000 * 5)
//        println("sleep")
//    }
    val test = jsbind.getMember("test")
    test.execute(listOf(1, 2), mapOf("a" to "b"))
}

class Abc {
    fun aa(m: Map<String, String>) {
        println(m["bb"]?.javaClass)
//        m.forEach { (t, u) -> println("${t.javaClass} $t  ${u?.javaClass} $u") }
        println("aa")
    }

    fun bb(m: Map<String, Any>) {
        println("map")
    }

    fun bb(l: List<Any>) {
        println("list")
    }
}
