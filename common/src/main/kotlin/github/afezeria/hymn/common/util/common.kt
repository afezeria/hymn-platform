package github.afezeria.hymn.common.util

import org.intellij.lang.annotations.Language
import org.slf4j.LoggerFactory

/**
 * @author afezeria
 */
fun Any?.println() {
    println(this)
}

fun String.msgById(value: Any): String {
    return "$this [id:$value]"
}

fun String.msgByPairs(vararg pairs: Pair<String, Any?>): String {
    return "$this [${pairs.joinToString(separator = ",") { "${it.first}:${it.second}" }}]"
}

fun String.msgByPair(field: String, value: Any?): String {
    return "$this [$field:$value]"
}
