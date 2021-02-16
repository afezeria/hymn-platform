package github.afezeria.hymn.core.module.controller

import github.afezeria.hymn.common.TestApplication
import github.afezeria.hymn.common.adminSession
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.util.toClass
import github.afezeria.hymn.core.module.entity.Account
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
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
    classes = [TestApplication::class],
)
internal class AccountControllerTest {

    @Autowired
    lateinit var client: OkHttpClient

    @LocalServerPort
    var port: Int = -1

    val url: String
        get() = "http://localhost:$port/module/core/api/v2104/account"

    @BeforeEach
    fun setUp() {
        mockkObject(Session)
        every { Session.getInstance() } returns adminSession
    }

    @Test
    fun findAll() {
        println("=========================")
        val httpUrl = HttpUrl.parse(url)!!.newBuilder()
            .build()
        val request = Request.Builder().url(httpUrl)
            .build()
        client.newCall(request).execute().use {
            val body = it.body()?.string()
            val list = it.body()?.string().toClass<List<Account>>()
            it.code() shouldBe 200
//            body shouldNotBe null
//            body!!.map { it.id } shouldContainExactly listOf(cid, bid, aid)
        }

    }

    @Test
    fun findById() {
    }

    @Test
    fun create() {
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }
}