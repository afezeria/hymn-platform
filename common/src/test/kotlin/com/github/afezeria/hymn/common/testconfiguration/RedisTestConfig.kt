package com.github.afezeria.hymn.common.testconfiguration

import com.github.afezeria.hymn.common.KGenericContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

/**
 * @author afezeria
 */
@TestConfiguration
class RedisTestConfig {
    companion object {
        var container: KGenericContainer
        var port: Int = 6379
        var host: String
        val password: String = "abc"

        init {
            container = KGenericContainer("redis:6.0.10-buster")
                .withCommand("redis-server", "--requirepass", password)
                .withExposedPorts(port)
            container.start()
            host = container.containerInfo.networkSettings.ipAddress
            println()
        }

//        这种形式放在TestConfiguration中不起作用
//        @JvmStatic
//        @DynamicPropertySource
//        fun properties(registry: DynamicPropertyRegistry) {
//
//            registry.add("spring.redis.host", { host });
//            registry.add("spring.redis.port", { port });
//            registry.add("spring.redis.password", { password });
//        }
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val configuration = RedisStandaloneConfiguration(host, port)
        configuration.password = RedisPassword.of(password)
        return LettuceConnectionFactory(configuration)
    }

}