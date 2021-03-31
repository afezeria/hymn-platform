package com.github.afezeria.hymn.script.platform

import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * @author afezeria
 */
abstract class SourceCache<V> {
    companion object {
        private val cacheManager: CacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .build(true)
    }

    private val lockCache: Cache<String, Lock> = cacheManager.createCache(
        this::class.java.simpleName,
        CacheConfigurationBuilder.newCacheConfigurationBuilder(
            String::class.javaObjectType,
            Lock::class.javaObjectType,
            ResourcePoolsBuilder.heap(1000)
        ).withExpiry(
            ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(30))
        )
    )
    private val sourceCache: MutableMap<String, V> = ConcurrentHashMap()

    fun clean(key: String) {
        val lock = ReentrantLock(true).let {
            lockCache.putIfAbsent(key, it) ?: it
        }
        if (lock.tryLock()) {
            try {
                sourceCache.remove(key)
            } finally {
                lock.unlock()
            }
        }
    }

    fun getOrPut(key: String, dataProvider: () -> V): V {
        var value = sourceCache[key]
        if (value == null) {
            val lock = ReentrantLock(true).let {
                lockCache.putIfAbsent(key, it) ?: it
            }
            if (lock.tryLock()) {
                try {
                    value = sourceCache[key]
                    if (value == null) {
                        value = dataProvider()
                        sourceCache[key] = value
                    }
                } finally {
                    lock.unlock()
                }
            }
        }
        return requireNotNull(value)
    }

}

/**
 * key为对象id
 */
object TriggerCache : SourceCache<List<TriggerSourceWithTime>>()

/**
 * key为接口api
 */
object ApiCache : SourceCache<SourceWithTime>()

/**
 * key为函数id
 */
object FunctionCache : SourceCache<SourceWithTime>()
