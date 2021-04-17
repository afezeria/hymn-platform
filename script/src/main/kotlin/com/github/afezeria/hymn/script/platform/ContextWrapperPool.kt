package com.github.afezeria.hymn.script.platform

import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.platform.Session
import org.apache.commons.pool2.impl.GenericObjectPool
import java.util.concurrent.*

private typealias AccountId = String
private typealias ThreadId = Long

/**
 * js上下文对象池
 * @author afezeria
 */
internal object ContextWrapperPool :
    GenericObjectPool<ContextWrapper>(ContextWrapperFactory()) {
    private val threadDept = ThreadLocal.withInitial { 0 }

    private val activeContext = ConcurrentHashMap<ThreadId, ContextWrapper>()

    private const val MAX_WAIT_MILLIS = 10 * 1000L

    /**
     * 监控activeContext中超时的context
     */
    private val monitorThreadPool = Executors.newSingleThreadScheduledExecutor()

    /**
     * 用于debug的context缓存
     */
    internal val debugWrapperCache = ConcurrentHashMap<AccountId, ContextWrapper>()

    private val debugContextTimeoutDetectionPool = ThreadPoolExecutor(
        5,
        5,
        0,
        TimeUnit.SECONDS,
        SynchronousQueue()
    ) { _, _ ->
        throw InnerException("一个节点最多允许5个用户同时debug")
    }

    init {
        maxTotal = 1000
        maxIdle = 1000
        testOnCreate = true
        testOnBorrow = true
        testOnReturn = true
        maxWaitMillis = MAX_WAIT_MILLIS
        monitorThreadPool.scheduleAtFixedRate(
            {
                //关闭超时的context
                val current = System.currentTimeMillis()
                for ((id, wrapper) in activeContext) {
                    if (current - wrapper.timestamp > 20 * 60 * 1000) {
                        activeContext.remove(id)
                        returnObject(wrapper)
                    }
                }
            }, 60, 60, TimeUnit.SECONDS
        )
    }


    fun <T> use(borrowMaxWaitMillis: Long = MAX_WAIT_MILLIS, fn: (ContextWrapper) -> T): T {
        val dept = threadDept.get()
        var context: ContextWrapper? = null
        val threadId = Thread.currentThread().id
        val accountId = Session.getInstance().accountId
        var debugFlag = false
        try {
            threadDept.set(dept + 1)
            context = debugWrapperCache[accountId]
            //如果不为null表示开启了debug模式
            if (context != null) {
                debugFlag = true
            } else {
                context = if (dept == 0) {
                    borrowObject(borrowMaxWaitMillis).also {
                        activeContext[threadId] = it
                    }
                } else {
                    requireNotNull(activeContext[threadId])
                }
            }
            return fn.invoke(context!!)
        } finally {
            threadDept.set(dept)
            if (dept == 0 && !debugFlag) {
                activeContext.remove(threadId)
                returnObject(context)
            }
        }
    }


    /**
     * 创建用于debug的context
     */
    fun createDebugContext(lanIp: String): String {
        val accountId = Session.getInstance().accountId
        debugContextTimeoutDetectionPool.execute {
            while (true) {
                Thread.sleep(1000 * 60)
                val wrapper = debugWrapperCache[accountId] ?: break
//                超过半小时未使用强制关闭
                if ((System.currentTimeMillis() - wrapper.timestamp) > 1800 * 1000) {
                    debugWrapperCache.remove(accountId)
                    wrapper.destroy()
                    break
                }
            }
        }
        val wrapper = ContextWrapper(
            debug = true,
            lanIp = lanIp,
        )
        wrapper.timestamp = System.currentTimeMillis()
        debugWrapperCache[accountId] = wrapper
        return wrapper.url
    }

    /**
     * 关闭用于debug的context
     */
    fun closeDebugContext() {
        val accountId = Session.getInstance().accountId
        debugWrapperCache.remove(accountId)?.apply { destroy() }
    }

    /**
     * 将wrapper对象返回给池
     * [obj] 是debugContext的场合下会关闭context
     */
    override fun returnObject(obj: ContextWrapper?) {
        obj?.apply {
            if (!debug) {
                timestamp = 0
                super.returnObject(this)
            } else {
                destroy()
            }
        }
    }
}
