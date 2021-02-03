package github.afezeria.hymn.common

import github.afezeria.hymn.common.platform.ConfigService
import github.afezeria.hymn.common.platform.PermService
import io.mockk.every
import io.mockk.mockk
import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import java.util.concurrent.TimeUnit


/**
 * @author afezeria
 */
@SpringBootApplication(scanBasePackages = ["github.afezeria.hymn"])
class TestApplication {

    @Bean
    fun okHttpClient(): OkHttpClient {
        val client =
            OkHttpClient.Builder()
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600,TimeUnit.SECONDS)
                .writeTimeout(600,TimeUnit.SECONDS)
                .build()
        return client
    }


    @Bean
    fun permService(): PermService {
        val mock = mockk<PermService>()
        every { mock.hasFieldPerm(any(), any()) } returns true
        every { mock.hasFunctionPerm(any(), any()) } returns true
        every { mock.hasDataPerm(any(), any()) } returns true
        every { mock.hasObjectPerm(any()) } returns true
        return mock
    }
}
