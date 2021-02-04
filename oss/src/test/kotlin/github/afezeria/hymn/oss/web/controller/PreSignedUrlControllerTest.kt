package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.TestApplication
import github.afezeria.hymn.common.adminSession
import github.afezeria.hymn.common.platform.CacheService
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import github.afezeria.hymn.common.util.Jwt
import github.afezeria.hymn.common.util.toClass
import github.afezeria.hymn.oss.OssCacheKey
import github.afezeria.hymn.oss.OssTestConfiguration
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import okhttp3.OkHttpClient
import okhttp3.Request
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
import java.util.*


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

    @Autowired
    lateinit var cacheService: CacheService

    @Autowired
    lateinit var client: OkHttpClient


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
            bucket = bucket,
            objectName = path,
            inputStream = ByteArrayInputStream("abc".toByteArray()),
            contentType = "application/octet-stream",
            tmp = false
        )
        val objectInfo = ossService.getObjectInfoById(fileId)!!
        val objectUrl = ossService.getObjectUrl(bucket, objectInfo.path)

        val url = "http://localhost:$port/$objectUrl"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use {
            val body = it.body()?.string()
            it.code() shouldBe 200
            body shouldNotBe null
            body shouldBe "abc"
            it.header("Content-Disposition") shouldBe "attachment;filename=\"abc.txt\""
        }
    }

    @Test
    fun `invalid token`() {
        val url = "http://localhost:$port/module/oss/api/v2104/public/pre-signed?token=   "

        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use {
            val body = it.body()?.string().toClass<Map<String, String>>()
            it.code() shouldBe 400
            body shouldNotBe null
            body!!["message"] shouldBe "无效的token"
        }
    }

    @Test
    fun `expired token`() {
        val bucket = "other"
        val path = "2021/01/01/abc.txt"
        val fileId = ossService.putObject(
            bucket = bucket,
            objectName = path,
            inputStream = ByteArrayInputStream("abc".toByteArray()),
            contentType = "application/octet-stream",
            tmp = false
        )
        val objectInfo = ossService.getObjectInfoById(fileId)!!
        val objectUrl = ossService.getObjectUrl(bucket, objectInfo.path, 1)

        val url = "http://localhost:$port/$objectUrl"

        Thread.sleep(2000)

        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use {
            val body: Map<String, String>? = it.body()?.string().toClass<Map<String, String>>()
            assertSoftly {
                it.code() shouldBe 400
                body shouldNotBe null
                body!!["message"] as String shouldMatch "token已于 .*? 过期".toRegex()
            }
        }
    }

    @Test
    fun `cache key was deleted`() {
        val bucket = "other"
        val path = "2021/01/01/abc.txt"
        val fileId = ossService.putObject(
            bucket = bucket,
            objectName = path,
            inputStream = ByteArrayInputStream("abc".toByteArray()),
            contentType = "application/octet-stream",
            tmp = false
        )
        val objectInfo = ossService.getObjectInfoById(fileId)!!
        val objectUrl = ossService.getObjectUrl(bucket, objectInfo.path)


//        删除缓存的key
        val token = Jwt.parseToken(objectUrl.substringAfter("token="))
        cacheService.delete(OssCacheKey.preSigned(token["id"] as String))

        val url = "http://localhost:$port/$objectUrl"
        Thread.sleep(2000)
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use {
            val body: Map<String, String>? = it.body()?.string().toClass<Map<String, String>>()
            assertSoftly {
                it.code() shouldBe 400
                body shouldNotBe null
                body!!["message"] shouldBe "token已过期"
            }
        }
    }

    @Test
    fun `file was deleted`() {
        val bucket = "other"
        val path = "2021/01/01/abc.txt"
        val fileId = ossService.putObject(
            bucket = "other",
            objectName = path,
            inputStream = ByteArrayInputStream("abc".toByteArray()),
            contentType = "application/octet-stream",
            tmp = false
        )
        val objectInfo = ossService.getObjectInfoById(fileId)!!
        val objectUrl = ossService.getObjectUrl(bucket, objectInfo.path)

//        删除文件
        ossService.removeObject(fileId)
        val url = "http://localhost:$port/$objectUrl"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use {
            val body: Map<String, String>? = it.body()?.string().toClass<Map<String, String>>()
            assertSoftly {
                it.code() shouldBe 400
                body shouldNotBe null
                body!!["message"] shouldBe "文件不存在"
            }

        }
    }
}
