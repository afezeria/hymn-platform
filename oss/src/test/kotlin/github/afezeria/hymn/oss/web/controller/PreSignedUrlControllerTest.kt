package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.TestApplication
import github.afezeria.hymn.common.adminSession
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import github.afezeria.hymn.oss.OssTestConfiguration
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.Comparator


/**
 * @author afezeria
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [TestApplication::class, OssTestConfiguration::class, RedisTestConfig::class],
)
internal class PreSignedUrlControllerTest : BaseDbTest() {
    companion object {
        @AfterAll
        @JvmStatic
        fun after() {
            Files.walk(Path.of(System.getProperty("user.dir") + "/hymn_storage")).use { walk ->
                walk.sorted(Comparator.reverseOrder())
                    .map { obj: Path -> obj.toFile() }
                    .peek { println("delete $it") }
                    .forEach(File::delete)
            }
        }
    }

    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var ossService: OssService

    @BeforeEach
    fun setUp() {
        mockkObject(Session)
        every { Session.getInstance() } returns adminSession
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun greetingShouldReturnDefaultMessage() {
        val bucket = "other"
        val path = "2021/01/01/abc.txt"
        val fileId = ossService.putObject(
            bucket = "other",
            objectName = path,
            inputStream = ByteArrayInputStream("abc".toByteArray()),
            contentType = "application/octet-stream",
            tmp = false
        )
        val objectInfo= ossService.getObjectInfoById(fileId)!!
        val objectUrl = ossService.getObjectUrl(bucket, objectInfo.path)
        objectUrl shouldNotBe ""
        val serverUrl = "http://localhost:$port/$objectUrl"
        println(serverUrl)
        while (true){
            Thread.sleep(1000)
            println("test")
        }
        println()
//        restTemplate.get
//        val resp = restTemplate.postForEntity(serverUrl, requestEntity, String::class.java)
//        resp.statusCodeValue shouldBe 200
//        resp.body!!.length shouldBe 32
    }


}