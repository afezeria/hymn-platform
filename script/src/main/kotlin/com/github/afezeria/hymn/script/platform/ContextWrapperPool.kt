package com.github.afezeria.hymn.script.platform

import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.platform.Session
import org.apache.commons.pool2.impl.GenericObjectPool
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author afezeria
 */
internal object ContextWrapperPool :
    GenericObjectPool<ContextWrapper>(ContextWrapperFactory()) {
    init {
        maxTotal = 1000
        maxIdle = 1000
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
                borrowObject(borrowMaxWaitMillis).also { threadContext.set(it) }
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

    data class WrapperWithTimestamp(var timestamp: Long, val wrapper: ContextWrapper)

    internal val debugContextCache = ConcurrentHashMap<String, WrapperWithTimestamp>()
    private val debugContextTimeoutDetectionPool = ThreadPoolExecutor(
        5,
        5,
        0,
        TimeUnit.SECONDS,
        SynchronousQueue()
    ) { _, _ ->
        throw InnerException("一个节点最多允许5个用户同时debug")
    }

    fun createDebugContext(lanIp: String): String {
        val accountId = Session.getInstance().accountId
        debugContextTimeoutDetectionPool.execute {
            while (true) {
                Thread.sleep(1000 * 60)
                val (timestamp, wrapper) = debugContextCache[accountId] ?: break
//                超过半小时未使用强制关闭
                if ((System.currentTimeMillis() - timestamp) > 1800 * 1000) {
                    debugContextCache.remove(accountId)
                    wrapper.destroy()
                    break
                }
            }
        }
        val wrapper = WrapperWithTimestamp(
            System.currentTimeMillis(),
            ContextWrapper(
                debug = true,
                lanIp = lanIp,
            )
        )
        debugContextCache[accountId] = wrapper
        return wrapper.wrapper.url
    }

    fun closeDebugContext() {
        val accountId = Session.getInstance().accountId
        debugContextCache.remove(accountId)?.apply { wrapper.destroy() }
    }

    override fun borrowObject(borrowMaxWaitMillis: Long): ContextWrapper {
        val accountId = Session.getInstance().accountId
        return debugContextCache[accountId]?.let {
            if (!it.wrapper.available()) {
                debugContextCache.remove(accountId)
                it.wrapper.destroy()
                null
            } else {
                it.timestamp = System.currentTimeMillis()
                it.wrapper
            }
        } ?: super.borrowObject(borrowMaxWaitMillis)
    }

    override fun returnObject(obj: ContextWrapper?) {
        obj?.apply {
//        debug用context不需要返还给池
            if (!debug) {
                super.returnObject(this)
            }
        }
    }
}
