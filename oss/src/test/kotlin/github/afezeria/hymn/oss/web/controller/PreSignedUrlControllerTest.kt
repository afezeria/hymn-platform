package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort


/**
 * @author afezeria
 */
//@Import(OssTestConfiguration::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [RedisTestConfig::class]
)
internal class PreSignedUrlControllerTest {
    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun greetingShouldReturnDefaultMessage() {
        assertThat(
            restTemplate.getForObject(
                "http://localhost:" + port.toString() + "/",
                String::class.java
            )
        ).contains("Hello, World")
    }


}