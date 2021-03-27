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
            String::class.java,
            Lock::class.java,
            ResourcePoolsBuilder.heap(1000)
        ).withExpiry(
            ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(30))
        )
    )
    private val sourceCache: MutableMap<String, V> = ConcurrentHashMap()

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

object TriggerCache : SourceCache<List<TriggerSourceWithTime>>()

object ApiCache : SourceCache<SourceWithTime>()

object FunctionCache : SourceCache<SourceWithTime>()
