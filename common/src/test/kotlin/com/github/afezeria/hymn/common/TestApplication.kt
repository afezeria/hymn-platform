package com.github.afezeria.hymn.common

import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import java.util.concurrent.TimeUnit


/**
 * @author afezeria
 */
@SpringBootApplication(scanBasePackages = ["com.github.afezeria.hymn"])
class TestApplication {

    @Bean
    fun okHttpClient(): OkHttpClient {
        val client =
            OkHttpClient.Builder()
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .build()
        return client
    }


}
