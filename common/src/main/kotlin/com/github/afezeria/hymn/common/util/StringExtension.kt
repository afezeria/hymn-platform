package com.github.afezeria.hymn.common.util

import java.util.*

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
        it.value.last().uppercaseChar().toString()
    }
}

fun String.camelCase(): String {
    return pascalCase().replaceFirstChar { it.lowercase(Locale.getDefault()) }
}

private val underscoreRegex = Regex("([a-z]+|[A-Z][a-z]+|[A-Z]+|[0-9]+)")
fun String.snakeCase(): String {
    return underscoreRegex.findAll(this).map { it.value.lowercase(Locale.getDefault()) }.joinToString("_")
}

fun String.dashCase(): String {
    return this.snakeCase().replace("_", "-")
}
