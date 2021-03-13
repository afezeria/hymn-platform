package com.github.afezeria.hymn.common.lazy

import kotlin.concurrent.getOrSet

/**
 * @author afezeria
 */
internal object LazyAction {
    val actions = ThreadLocal<MutableList<() -> Any?>>()
    fun addAction(fn: () -> Any?) {
        actions.getOrSet { mutableListOf() }
            .add(fn)
    }
}
