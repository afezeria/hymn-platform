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
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.withLock
import kotlin.concurrent.write

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
    private var sourceCache: MutableMap<String, V> = ConcurrentHashMap()
    private val globalCacheLock = ReentrantReadWriteLock(true)

    fun cleanAllCache() {
        globalCacheLock.write {
            sourceCache = ConcurrentHashMap()
        }
    }

    fun clean(key: String) {
        globalCacheLock.read {
            ReentrantLock(true).let {
                lockCache.putIfAbsent(key, it) ?: it
            }.withLock {
                sourceCache.remove(key)
            }
        }
    }

    fun getOrPut(key: String, dataProvider: () -> V): V {
        globalCacheLock.read {
            var value: V? = sourceCache[key]
            if (value == null) {
                ReentrantLock(true).let {
                    lockCache.putIfAbsent(key, it) ?: it
                }.withLock {
                    value = sourceCache[key]
                    if (value == null) {
                        value = dataProvider()
                        sourceCache[key] = requireNotNull(value)
                    }
                }
            }
            return requireNotNull(value)
        }
    }

}

/**
 * 触发器代码缓存
 * key为对象id
 */
object TriggerCache : SourceCache<List<TriggerSourceWithTime>>()

/**
 * 自定义api代码缓存
 * key为接口api
 */
object ApiCache : SourceCache<SourceWithTime>()

/**
 * 自定义函数代码缓存
 * key为函数id
 */
object FunctionCache : SourceCache<SourceWithTime>()
