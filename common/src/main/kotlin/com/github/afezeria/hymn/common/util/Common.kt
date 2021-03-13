package com.github.afezeria.hymn.common.util

import java.util.*

/**
 * @author afezeria
 */
fun Any?.println() {
    println(this)
}

fun randomUUIDStr(): String = UUID.randomUUID().toString().replace("-", "")
