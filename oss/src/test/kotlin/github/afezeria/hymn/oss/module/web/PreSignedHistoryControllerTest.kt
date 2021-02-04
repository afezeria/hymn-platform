package github.afezeria.hymn.oss.module.web

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.randomUUIDStr
import github.afezeria.hymn.common.util.toClass
import github.afezeria.hymn.oss.OssTestConfiguration
import github.afezeria.hymn.oss.module.entity.PreSignedHistory
import github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
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
internal class PreSignedHistoryControllerTest {
    companion object {
        val id1 = randomUUIDStr()
        val id2 = randomUUIDStr()
        val id3 = randomUUIDStr()
        val aid = randomUUIDStr()
        val bid = randomUUIDStr()
        val cid = randomUUIDStr()
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
            it.execute(
                """
                insert into hymn.oss_pre_signed_history (id, file_id, expiry, create_date, create_by_id, create_by)
                values ('$id1','$aid',3000,'2021-01-01 14:34:55.466333'::timestamptz,'$DEFAULT_ACCOUNT_ID','$DEFAULT_ACCOUNT_NAME'),
                       ('$id2','$aid',3000,'2021-01-02 14:34:55.466333'::timestamptz,'$DEFAULT_ACCOUNT_ID','$DEFAULT_ACCOUNT_NAME'),
                       ('$id3','$bid',3000,'2021-01-03 14:34:55.466333'::timestamptz,'$DEFAULT_ACCOUNT_ID','$DEFAULT_ACCOUNT_NAME');
                ;
            """
            )
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        adminConn.use {
            it.execute("delete from hymn.oss_file_record")
            it.execute("delete from hymn.oss_pre_signed_history")
        }
    }

    @Autowired
    lateinit var preSignedHistoryService: PreSignedHistoryService

    @Autowired
    lateinit var client: OkHttpClient

    @Test
    fun findByFileId() {
        val url = "http://localhost:$port/module/oss/api/v2104/pre-signed-history"
        val httpUrl = HttpUrl.parse(url)!!.newBuilder()
            .addQueryParameter("fileId", aid)
            .build()
        val request = Request.Builder().url(httpUrl)
            .build()
        client.newCall(request).execute().use {
            val body = it.body()?.string().toClass<List<PreSignedHistory>>()
            it.code() shouldBe 200
            body shouldNotBe null
            body!!.map { it.id } shouldContainExactlyInAnyOrder listOf(id1, id2)
        }
    }

    @Test
    fun findByDate() {
        val url = "http://localhost:$port/module/oss/api/v2104/pre-signed-history"
        val httpUrl = HttpUrl.parse(url)!!.newBuilder()
            .addQueryParameter("startDate", "2021-01-02T14:34:55")
            .build()
        val request = Request.Builder().url(httpUrl)
            .build()
        client.newCall(request).execute().use {
            val body = it.body()?.string().toClass<List<PreSignedHistory>>()
            it.code() shouldBe 200
            body shouldNotBe null
            body!!.map { it.id } shouldContainExactlyInAnyOrder listOf(id2, id3)
        }
    }

    @Test
    fun deleteByIds() {
        val url = "http://localhost:$port/module/oss/api/v2104/pre-signed-history"
        val httpUrl = HttpUrl.parse(url)!!.newBuilder()
            .addQueryParameter("ids", listOf(id1, id2).joinToString(","))
            .build()
        val request = Request.Builder().url(httpUrl)
            .delete()
            .build()
        client.newCall(request).execute().use {
            val body = it.body()?.string().toClass<Int>()
            it.code() shouldBe 200
            body shouldBe 2
        }
    }
}