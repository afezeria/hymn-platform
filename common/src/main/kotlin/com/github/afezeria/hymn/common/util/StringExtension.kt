package com.github.afezeria.hymn.common.util

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

fun String.pascalCase(): String {
    return this.replace(camelizeRegex) {
        it.value.last().toUpperCase().toString()
    }
}

fun String.camelCase(): String {
    return pascalCase().decapitalize()
}

private val underscoreRegex = Regex("([a-z]+|[A-Z][a-z]+|[A-Z]+|[0-9]+)")
fun String.snakeCase(): String {
    return underscoreRegex.findAll(this).map { it.value.toLowerCase() }.joinToString("_")
}

fun String.dashCase(): String {
    return this.snakeCase().replace("_", "-")
}
