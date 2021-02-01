package github.afezeria.hymn.common

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory
import org.springframework.web.client.RestTemplate


/**
 * @author afezeria
 */
@SpringBootApplication(scanBasePackages = ["github.afezeria.hymn"])
class TestApplication {
    @Bean
    fun restTemplate(): RestTemplate {
        val timeout = 1800000
        val okHttp3ClientHttpRequestFactory = OkHttp3ClientHttpRequestFactory()
        okHttp3ClientHttpRequestFactory.setConnectTimeout(timeout)
        return RestTemplate(okHttp3ClientHttpRequestFactory)

    }
}
