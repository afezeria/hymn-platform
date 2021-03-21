package com.github.afezeria.hymn.script

import org.apache.commons.pool2.impl.GenericObjectPool

/**
 * @author afezeria
 */
internal object ContextWrapperPool :
    GenericObjectPool<ContextWrapper>(ContextWrapperFactory()) {
    init {
        testOnCreate = true
        testOnBorrow = true
        testOnReturn = true
        maxWaitMillis = 10 * 1000
    }

    private val threadDept = ThreadLocal.withInitial { 0 }
    private val threadContext = ThreadLocal<ContextWrapper>()

    fun <T> use(borrowMaxWaitMillis: Long = maxWaitMillis, fn: (ContextWrapper) -> T): T {
        val dept = threadDept.get()
        var context: ContextWrapper? = null
        try {
            threadDept.set(dept + 1)
            context = if (dept == 0) {
                borrowObject(borrowMaxWaitMillis)!!.also { threadContext.set(it) }
            } else {
                requireNotNull(threadContext.get())
            }
            return fn.invoke(context)
        } finally {
            threadDept.set(dept)
            if (dept == 0) {
                threadContext.set(null)
                returnObject(context)
            }
        }
    }
}
