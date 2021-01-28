package github.afezeria.hymn.common.lazy

import kotlin.concurrent.getOrSet
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberFunctions

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
