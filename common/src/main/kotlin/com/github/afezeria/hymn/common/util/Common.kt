package com.github.afezeria.hymn.common.util

import java.io.PrintWriter
import java.io.StringWriter
import java.time.OffsetDateTime
import java.util.*

/**
 * @author afezeria
 */
fun Any?.println() {
    println(this)
}

val DEFAULT_OFFSET = OffsetDateTime.now().offset

fun randomUUIDStr(): String = UUID.randomUUID().toString().replace("-", "")

fun Throwable.getTrace(): String {
    val stringWriter = StringWriter()
    val writer = PrintWriter(stringWriter)
    printStackTrace(writer)
    val buffer = stringWriter.buffer
    return buffer.toString()
}