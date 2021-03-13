package com.github.afezeria.hymn.common.platform

import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.lazy.LazyAction
import com.github.afezeria.hymn.common.util.mapper
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * 缓存服务
 * 方法参数中的 expiry 单位为秒
 * @author afezeria
 */
interface CacheService {
    /**
     * 获取 [key] 对应的值
     */
    fun get(key: String): String?

    fun multiGet(keys: Collection<String>): List<String>

    /**
     * 获取 [key] 对应的值并转换成 [T] 类型
     */
    fun <T> getAs(key: String, clazz: Class<T>): T? {
        return get(key)?.run {
            mapper.readValue(this, clazz)
        }
    }

    fun <T> multiGetAs(keys: Collection<String>, clazz: Class<T>): List<T> {
        return multiGet(keys).map {
            mapper.readValue(it, clazz)
        }
    }

    /**
     * 请求正常结束后绑定 [value] 到 [key]
     */
    fun lazySet(key: String, value: Any, expiry: Long = 1800) {
        LazyAction.addAction { set(key, value, expiry) }
    }

    /**
     * 请求正常结束且 [key] 不存在时绑定 [value] 到 [key]
     */
    fun lazySetIfAbsent(key: String, value: Any, expiry: Long = 1800) {
        LazyAction.addAction { set(key, value, expiry) }
    }

    /**
     * 请求正常结束且 [key] 存在时绑定 [value] 到 [key]
     */
    fun lazySetIfPresent(key: String, value: Any, expiry: Long = 1800) {
        LazyAction.addAction { set(key, value, expiry) }
    }

    /**
     * 绑定 [value] 到 [key]
     */
    fun set(key: String, value: Any, expiry: Long = 1800)

    /**
     * [key] 不存在时绑定 [value] 到 [key]
     */
    fun setIfAbsent(key: String, value: Any, expiry: Long = 1800): Boolean

    /**
     * [key] 存在时绑定 [value] 到 [key]
     */
    fun setIfPresent(key: String, value: Any, expiry: Long = 1800): Boolean

    /**
     * 绑定 [value] 到 [key] 并返回当前值
     */
    fun getAndSet(key: String, value: Any): String?

    /**
     * 绑定 [value] 到 [key] 同时返回当前值并转换成 [T] 类型
     */
    fun <T : Any> getAndSet(key: String, value: T, clazz: Class<T>): T? {
        val str = getAndSet(key, value) ?: return null
        return mapper.readValue(str, clazz)
    }

    /**
     * 更新 [key] 的过期时间
     */
    fun setExpire(key: String, expiry: Long = 1800): Boolean

    /**
     * 更新 [key] 的过期时间
     */
    fun setExpireAt(key: String, dateTime: LocalDateTime): Boolean {
        val between = ChronoUnit.SECONDS.between(LocalDateTime.now(), dateTime)
        return setExpire(key, between)
    }

    /**
     * 获取指定 [key] 的剩余时间
     */
    fun getExpire(key: String): Long?

    fun delete(key: String): Boolean
    fun deleteAll(keys: Collection<String>): Long

    /**
     * 执行具体实现的特殊函数
     * @param name 函数名称
     * @param classList 参数类型列表
     * @param params 参数
     */
    fun exec(name: String, classList: List<Class<*>>, vararg params: Any?): Any? {
        helper()?.run {
            val method = this.javaClass.getMethod(name, *classList.toTypedArray())
            return method.invoke(this, *params)
        } ?: throw InnerException("实现类 ${this::class.qualifiedName} 未重载 helper() 方法")
    }

    fun helper(): Any? {
        return null
    }

    /**
     * 获取具体实现的客户端
     */
    fun getClient(): Any

}

inline fun <reified T> CacheService.getAs(key: String): T? {
    return getAs(key, T::class.java)
}

inline fun <reified T : Any> CacheService.getAndSetAs(key: String, value: T): T? {
    return getAndSet(key, value, T::class.java)
}

