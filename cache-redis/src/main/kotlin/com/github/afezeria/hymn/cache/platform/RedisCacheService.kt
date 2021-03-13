package com.github.afezeria.hymn.cache.platform

import com.github.afezeria.hymn.common.platform.CacheService
import com.github.afezeria.hymn.common.util.toClass
import com.github.afezeria.hymn.common.util.toJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

/**
 * @author afezeria
 */
@Service
class RedisCacheService : CacheService {
    @Autowired
    lateinit var template: StringRedisTemplate
    override fun get(key: String): String? {
        return template.opsForValue().get(key)?.toClass()
    }

    override fun multiGet(keys: Collection<String>): List<String> {
        return requireNotNull(template.opsForValue().multiGet(keys))
    }

    override fun set(key: String, value: Any, expiry: Long) {
        template.opsForValue().set(key, value.toJson(), expiry, TimeUnit.SECONDS)
    }

    override fun setIfAbsent(key: String, value: Any, expiry: Long): Boolean {
        return requireNotNull(
            template.opsForValue().setIfAbsent(key, value.toJson(), expiry, TimeUnit.SECONDS)
        )
    }

    override fun setIfPresent(key: String, value: Any, expiry: Long): Boolean {
        return requireNotNull(
            template.opsForValue().setIfPresent(key, value.toJson(), expiry, TimeUnit.SECONDS)
        )
    }

    override fun getAndSet(key: String, value: Any): String? {
        return template.opsForValue().getAndSet(key, value.toJson())
    }

    override fun setExpire(key: String, expiry: Long): Boolean {
        return template.expire(key, expiry, TimeUnit.SECONDS)
    }

    override fun getExpire(key: String): Long? {
        return template.getExpire(key)
    }

    override fun delete(key: String): Boolean {
        return template.delete(key)
    }

    override fun deleteAll(keys: Collection<String>): Long {
        return template.delete(keys)
    }

    override fun getClient(): Any {
        return template
    }
}
