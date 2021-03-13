package com.github.afezeria.hymn.oss

import com.github.afezeria.hymn.common.platform.ConfigService
import com.github.afezeria.hymn.common.util.toJson
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

/**
 * @author afezeria
 */
@TestConfiguration

class OssTestConfiguration {
    @Bean
    fun config(): ConfigService {
        val mock = mockk<ConfigService>()
        every { mock.getAsString("oss") } returns OssConfigProperties(prefix = "abc").toJson()
        return mock
    }
}