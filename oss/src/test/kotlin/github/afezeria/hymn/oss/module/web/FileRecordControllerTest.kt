package github.afezeria.hymn.oss.module.web

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.randomUUIDStr
import github.afezeria.hymn.common.util.toClass
import github.afezeria.hymn.common.util.toJson
import github.afezeria.hymn.oss.OssTestConfiguration
import github.afezeria.hymn.oss.module.dto.FileRecordDto
import github.afezeria.hymn.oss.module.entity.FileRecord
import github.afezeria.hymn.oss.module.service.FileRecordService
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import okhttp3.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort

/**
 * @author afezeria
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [TestApplication::class, OssTestConfiguration::class, RedisTestConfig::class],
)
internal class FileRecordControllerTest {
    companion object {
        val aid = "9c7d320e50d34d8d95c051bcd120a79a"
        val bid = "c899557f496c410794c1803d1eef1f8d"
        val cid = "e37229bdc1c043b48ff34aac911f5004"
    }

    @LocalServerPort
    var port: Int = -1

    @BeforeEach
    fun setUp() {
        mockkObject(Session)
        every { Session.getInstance() } returns adminSession
        adminConn.use {
            it.execute(
                """
                    insert into hymn.oss_file_record (id,bucket, file_name, content_type, path, create_by_id, create_by, modify_by_id,
                                  modify_by, create_date, modify_date)
                    values ('$aid', 'other', 'abc1.txt', 'application/octet-streaom','2020/01/01/abc1.txt','$DEFAULT_ACCOUNT_ID','$DEFAULT_ACCOUNT_NAME','$DEFAULT_ACCOUNT_ID','$DEFAULT_ACCOUNT_NAME','2021-01-01 14:34:55.466333'::timestamptz,now()),
                           ('$bid', 'other', 'abc2.txt', 'application/octet-streaom','2020/01/01/abc2.txt','$DEFAULT_ACCOUNT_ID','$DEFAULT_ACCOUNT_NAME','$DEFAULT_ACCOUNT_ID','$DEFAULT_ACCOUNT_NAME','2021-01-02 14:34:55.466333'::timestamptz,now()),
                           ('$cid', 'other', 'abc3.txt', 'application/octet-streaom','2020/01/01/abc3.txt','$DEFAULT_ACCOUNT_ID','$DEFAULT_ACCOUNT_NAME','$DEFAULT_ACCOUNT_ID','$DEFAULT_ACCOUNT_NAME','2021-01-03 14:34:55.466333'::timestamptz,now());
                """
            )
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        adminConn.use {
            it.execute("delete from hymn.oss_file_record")
        }
    }

    @Autowired
    lateinit var fileRecordService: FileRecordService

    @Autowired
    lateinit var client: OkHttpClient

    @Test
    fun findAll() {
        val url = "http://localhost:$port/module/oss/api/v2104/file-record"
        val httpUrl = HttpUrl.parse(url)!!.newBuilder()
            .build()
        val request = Request.Builder().url(httpUrl)
            .build()
        client.newCall(request).execute().use {
            val body = it.body()?.string().toClass<List<FileRecord>>()
            it.code() shouldBe 200
            body shouldNotBe null
            body!!.map { it.id } shouldContainExactly listOf(cid, bid, aid)
        }
    }

    @Test
    fun pageFind() {
        val url = "http://localhost:$port/module/oss/api/v2104/file-record"
        val httpUrl = HttpUrl.parse(url)!!.newBuilder()
            .addQueryParameter("pageSize", "2")
            .addQueryParameter("pageNum", "2")
            .build()
        val request = Request.Builder().url(httpUrl)
            .build()
        client.newCall(request).execute().use {
            val body = it.body()?.string().toClass<List<FileRecord>>()
            it.code() shouldBe 200
            body shouldNotBe null
            body!!.map { it.id } shouldContainExactly listOf(aid)
        }
    }

    @Test
    fun findById() {
        val url = "http://localhost:$port/module/oss/api/v2104/file-record/${aid}"
        val httpUrl = HttpUrl.parse(url)!!.newBuilder()
            .build()
        val request = Request.Builder().url(httpUrl)
            .build()
        client.newCall(request).execute().use {
            val body = it.body()?.string().toClass<FileRecord>()
            assertSoftly {
                it.code() shouldBe 200
                body shouldNotBe null
                body!!.id shouldBe aid
            }
        }
    }

    @Test
    fun `find by a nonexistent id`() {
        val url = "http://localhost:$port/module/oss/api/v2104/file-record/${randomUUIDStr()}"
        val httpUrl = HttpUrl.parse(url)!!.newBuilder()
            .build()
        val request = Request.Builder().url(httpUrl)
            .build()
        client.newCall(request).execute().use {
            val body = it.body()?.string().toClass<Map<String, String>>()
            assertSoftly {
                it.code() shouldBe 404
                body shouldNotBe null
                body!!["message"] shouldBe "文件记录不存在"
            }
        }
    }

    @Test
    fun update() {
        val dto = FileRecordDto(
            bucket = "other",
            fileName = "bbb",
            contentType = null,
            path = "",
            objectId = null,
            fieldId = null,
            dataId = null,
            size = 0,
            tmp = null,
            visibility = null,
            remark = null
        )

        val url = "http://localhost:$port/module/oss/api/v2104/file-record/${aid}"
        val mediaType = MediaType.parse("application/json; charset=utf-8")

        val body = RequestBody.create(mediaType, dto.toJson()) // new
        val request = Request.Builder()
            .url(url)
            .put(body)
            .build()
        client.newCall(request).execute().use {
            val body = it.body()!!.string().toClass<Int>()
            it.code() shouldBe 200
            body shouldNotBe null
            body shouldBe 1
        }
        fileRecordService.findById(aid)!!.apply {
            fileName shouldBe "bbb"
        }
    }

    @Test
    fun delete() {
        val url = "http://localhost:$port/module/oss/api/v2104/file-record/${aid}"

        val request = Request.Builder()
            .url(url)
            .delete()
            .build()
        client.newCall(request).execute().use {
            val body = it.body()!!.string().toClass<Int>()
            it.code() shouldBe 200
            body shouldBe 1
        }
        fileRecordService.findById(aid) shouldBe null
    }
}