package github.afezeria.hymn.common.util

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.InnerException

/**
 * @author afezeria
 */


fun String.msgById(value: Any): String {
    return "$this [id:$value]"
}

fun String.msgByPairs(vararg pairs: Pair<String, Any?>): String {
    return "$this [${pairs.joinToString(separator = ",") { "${it.first}:${it.second}" }}]"
}

fun String.msgByPair(field: String, value: Any?): String {
    return "$this [$field:$value]"
}

private val camelizeRegex = Regex("(.?(?<![a-zA-Z0-9])\\w)")

fun String.camelize(): String {
    return this.replace(camelizeRegex) {
        it.value.last().toUpperCase().toString()
    }
}

fun String.lCamelize(): String {
    return camelize().decapitalize()
}

private val underscoreRegex = Regex("([a-z]+|[A-Z][a-z]+|[A-Z]+|[0-9]+)")
fun String.underscore(): String {
    return underscoreRegex.findAll(this).map { it.value.toLowerCase() }.joinToString("_")
}

fun String.dasherize(): String {
    return this.underscore().replace("_", "-")
}
