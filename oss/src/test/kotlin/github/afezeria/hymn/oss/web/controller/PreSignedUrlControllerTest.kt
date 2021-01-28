package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.TestApplication
import github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import github.afezeria.hymn.common.util.randomUUIDStr
import github.afezeria.hymn.oss.OssTestConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.nio.file.Files
import java.nio.file.StandardOpenOption


/**
 * @author afezeria
 */
//@Import(OssTestConfiguration::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [TestApplication::class, OssTestConfiguration::class, RedisTestConfig::class],
)
internal class PreSignedUrlControllerTest : BaseDbTest() {
    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun greetingShouldReturnDefaultMessage() {

    }


}