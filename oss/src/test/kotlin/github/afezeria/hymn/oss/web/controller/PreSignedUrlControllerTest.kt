package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.TestApplication
import github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import github.afezeria.hymn.oss.OssTestConfiguration
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
    classes = [TestApplication::class,OssTestConfiguration::class, RedisTestConfig::class],
)
internal class PreSignedUrlControllerTest {
    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun greetingShouldReturnDefaultMessage() {
        val resourceAsStream = this::class.java.classLoader.getResourceAsStream("test-zip.zip")
//        val classPathResource =
//            ClassPathResource("github/afezeria/hymn/oss/web/controller/PreSignedUrlController.class")
        println()
//        val headers= HttpHeaders()
//        headers.contentType= MediaType.MULTIPART_FORM_DATA
//        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
//
////        body.add("file", )
//
//        assertThat(
//            restTemplate.getForObject(
//                "http://localhost:" + port.toString() + "/",
//                String::class.java
//            )
//        ).contains("Hello, World")
    }


}