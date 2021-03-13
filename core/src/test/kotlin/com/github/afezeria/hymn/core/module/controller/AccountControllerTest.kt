//package com.github.afezeria.hymn.core.module.controller
//
//import com.github.afezeria.hymn.common.TestApplication
//import com.github.afezeria.hymn.common.adminSession
//import com.github.afezeria.hymn.common.platform.Session
//import com.github.afezeria.hymn.common.util.toClass
//import com.github.afezeria.hymn.core.module.entity.Account
//import io.kotest.assertions.throwables.shouldNotThrow
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.shouldNotBe
//import io.mockk.every
//import io.mockk.mockkObject
//import okhttp3.HttpUrl
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.web.server.LocalServerPort
//
///**
// * @author afezeria
// */
//@SpringBootTest(
//    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
//    classes = [TestApplication::class],
//)
//internal class AccountControllerTest {
//
//    @Autowired
//    lateinit var client: OkHttpClient
//
//    @LocalServerPort
//    var port: Int = -1
//
//    val url: String
//        get() = "http://localhost:$port/module/core/api/v2104/account"
//
//    @BeforeEach
//    fun setUp() {
//        mockkObject(Session)
//        every { Session.getInstance() } returns adminSession
//    }
//
//    @Test
//    fun findAll() {
//        val httpUrl = HttpUrl.parse(url)!!.newBuilder()
//            .build()
//        val request = Request.Builder().url(httpUrl)
//            .build()
//        client.newCall(request).execute().use {
//            val body = it.body()?.string()
//            it.code() shouldBe 200
//            body shouldNotBe null
//            shouldNotThrow<Exception> {
//                it.body()?.string().toClass<List<Account>>()
//            }
//        }
//    }
//
//    @Test
//    fun findById() {
//    }
//
//    @Test
//    fun create() {
//    }
//
//    @Test
//    fun update() {
//    }
//
//    @Test
//    fun delete() {
//    }
//}