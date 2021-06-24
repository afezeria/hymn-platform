package com.github.afezeria.hymn.common.conf

import com.github.afezeria.hymn.common.util.UidGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @author afezeria
 * date 2021/6/10 上午11:37
 */
@Configuration
class UidGeneratorConfig {
    @Value("\${hymn.workerId}")
    var workerId: Int = 0

    @Value("\${hymn.datacenterId}")
    var datacenterId: Int = 0

    @Value("\${hymn.utcTimeStr}")
    lateinit var utcTimeStr: String

    @Bean
    fun uidGenerator(): UidGenerator {
        return UidGenerator(workerId, datacenterId, utcTimeStr)
    }
}