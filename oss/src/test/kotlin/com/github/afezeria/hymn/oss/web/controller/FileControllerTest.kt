package com.github.afezeria.hymn.oss.web.controller

import com.github.afezeria.hymn.common.BaseDbTest
import com.github.afezeria.hymn.common.TestApplication
import com.github.afezeria.hymn.common.adminSession
import com.github.afezeria.hymn.common.platform.OssService
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import com.github.afezeria.hymn.common.util.toClass
import com.github.afezeria.hymn.oss.OssTestConfiguration
import com.github.afezeria.hymn.oss.contentType2Bucket
import com.github.afezeria.hymn.oss.filename2ContentType
import com.github.afezeria.hymn.oss.module.service.FileRecordService
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
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [TestApplication::class, OssTestConfiguration::class, RedisTestConfig::class],
)
internal class FileControllerTest : BaseDbTest() {

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
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var fileRecordService: FileRecordService

    @BeforeEach
    fun setUp() {
        mockkObject(Session.Companion)
        every { Session.getInstance() } returns adminSession
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Autowired
    lateinit var ossService: OssService

    @Autowired
    lateinit var client: OkHttpClient

    @Test
    fun tmpFileUpload() {
        val now = LocalDateTime.now()
        val requestEntity = createRequestEntity()
        val serverUrl = "http://localhost:$port/module/oss/api/tmp-file"
        val resp = restTemplate.postForEntity(serverUrl, requestEntity, String::class.java)
        resp.statusCodeValue shouldBe 200
        resp.body!!.length shouldBe 32
        val filename = (requestEntity.body!!.get("file")!!.get(0) as FileSystemResource).filename
        val contentType = filename2ContentType(filename)
        val bucket = contentType2Bucket(contentType)
        val fileRecord = fileRecordService.findById(resp.body!!)!!
        fileRecord shouldNotBe null
        fileRecord.contentType shouldBe contentType
        fileRecord.bucket shouldBe bucket
        fileRecord.path shouldMatch "${now.year}/${now.monthValue}/${now.dayOfMonth}/.*?$filename".toRegex()
        fileRecord.tmp shouldBe true
    }

    @Test
    fun `upload empty tmp file`() {
        val requestEntity = createRequestEntity(ByteArray(0))
        val serverUrl = "http://localhost:$port/module/oss/api/tmp-file"
        val resp = restTemplate.postForEntity(serverUrl, requestEntity, String::class.java)
        resp.statusCodeValue shouldBe 400
        resp.body shouldNotBe null
        resp.body.toClass<Map<String, String>>()!!.get("message") shouldBe "上传文件内容为空"
    }

    @Test
    fun fileUpload() {
        val now = LocalDateTime.now()
        val requestEntity = createRequestEntity()
        val serverUrl = "http://localhost:$port/module/oss/api/file"
        val resp = restTemplate.postForEntity(serverUrl, requestEntity, String::class.java)
        resp.statusCodeValue shouldBe 200
        resp.body!!.length shouldBe 32
        val filename = (requestEntity.body!!.get("file")!!.get(0) as FileSystemResource).filename
        val contentType = filename2ContentType(filename)
        val bucket = contentType2Bucket(contentType)
        val fileRecord = fileRecordService.findById(resp.body!!)!!
        fileRecord shouldNotBe null
        fileRecord.contentType shouldBe contentType
        fileRecord.bucket shouldBe bucket
        fileRecord.path shouldMatch "${now.year}/${now.monthValue}/${now.dayOfMonth}/.*?$filename".toRegex()
        fileRecord.tmp shouldBe false
    }

    @Test
    fun download() {
        val bucket = "other"
        val path = "2021/01/01/abc.txt"
        val fileId = ossService.putObject(
            bucket = bucket,
            objectName = path,
            inputStream = ByteArrayInputStream("abc".toByteArray()),
            contentType = "application/octet-stream",
            tmp = false
        )
        val url = "http://localhost:$port/module/oss/api/file/$fileId"

        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use {
            val body = it.body()?.string()
            assertSoftly {
                it.code() shouldBe 200
                body shouldNotBe null
                body shouldBe "abc"
                it.header("Content-Disposition") shouldBe "attachment;filename=\"abc.txt\""
            }
        }

    }

    fun createRequestEntity(
        content: ByteArray? = null,
        suffix: String? = null
    ): HttpEntity<MultiValueMap<String, Any>> {
        val headers = HttpHeaders()
        val tmp = Files.createTempFile("abc", suffix ?: ".txt")
        Files.write(tmp, content ?: "abc".toByteArray(), StandardOpenOption.WRITE)

        headers.contentType = MediaType.MULTIPART_FORM_DATA
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add("file", FileSystemResource(tmp))
        return HttpEntity(body, headers)
    }
}