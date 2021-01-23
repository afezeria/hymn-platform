package github.afezeria.hymn.oss

import github.afezeria.hymn.common.KGenericContainer
import github.afezeria.hymn.common.randomUUIDStr
import github.afezeria.hymn.oss.ftp.FTPClientFactory
import github.afezeria.hymn.oss.ftp.FTPConfig
import github.afezeria.hymn.oss.ftp.FTPOssService
import github.afezeria.hymn.oss.web.controller.SimpleFileController
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import mu.KLogging
import org.apache.commons.net.ftp.FTPClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.images.builder.Transferable
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.ByteArrayInputStream
import java.time.Duration
import java.util.*

/**
 * @author afezeria
 */
@Testcontainers
class FTPOssServiceTest {
    companion object : KLogging() {
        const val USERNAME = "admin"
        const val PASSWORD = "123456"
        lateinit var container: KGenericContainer
        lateinit var config: FTPConfig
        lateinit var ftp: FTPClient
        lateinit var service: FileService
        lateinit var fileController: SimpleFileController
        const val rootDir = "/home/vsftpd/admin/"

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            container = KGenericContainer("fauria/vsftpd")
                .withEnv(
                    mapOf(
                        "FTP_USER" to USERNAME,
                        "FTP_PASS" to PASSWORD,
                    )
                )
                .withExposedPorts(21)
                .waitingFor(
                    HostPortWaitStrategy()
                        .withStartupTimeout(Duration.ofSeconds(10))
                )
            container.start()
            config = FTPConfig().apply {
                host = container.containerInfo.networkSettings.ipAddress
                port = 21
                path = null
                username = USERNAME
                password = PASSWORD
                connectTimeout = 0
                bufferSize = 0
            }
            ftp = FTPClientFactory(config).create()
            fileController = mockk()
            service = FTPOssService(config)
        }

        @AfterAll
        @JvmStatic
        fun clear() {
            container.use {}
        }
    }


    @Test
    fun upload() {
        val str = "abc".toByteArray()
        service.putFile(
            "abc",
            "abc.txt",
            ByteArrayInputStream(str),
            "text/plain"
        )
        fileExist("abc/abc.txt") shouldBe true
        fileContent("abc/abc.txt") shouldBe str
    }

    @Test
    fun `upload with path`() {
        val str = "abc".toByteArray()
        service.putFile(
            "abc",
            "/2020/01/01/abc.txt",
            ByteArrayInputStream(str),
            "text/plain"
        )
        val p = "abc/2020/01/01/abc.txt"
        fileExist(p) shouldBe true
        fileContent(p) shouldBe str
    }


    @Test
    fun download() {
        val path = "dow/abc.txt"
        val byteArray = createFile(path)
        var array: ByteArray? = null
        service.getFile("dow", "abc.txt") {
            array = it.readAllBytes()
        }
        byteArray shouldBe array
    }

    @Test
    fun `move file in the same bucket`() {
        val path = "move/abc.txt"
        val byteArray = createFile(path)
        val bucket = "move"
        service.moveFile(bucket, "bcd.txt", bucket, "abc.txt")
        fileExist("move/abc.txt") shouldBe false
        fileExist("move/bcd.txt") shouldBe true
        fileContent("move/bcd.txt") shouldBe byteArray
    }

    @Test
    fun `move file between different bucket`() {
        val fa = randomUUIDStr()
        val fb = randomUUIDStr()
        val pa = "move/$fa"
        val pb = "move2/$fb"
        val byteArray = createFile(pb)
        service.moveFile("move", fa, "move2", fb)
        fileExist(pb) shouldBe false
        fileExist(pa) shouldBe true
        fileContent(pa) shouldBe byteArray
    }

    @Test
    fun `copy file in the same bucket`() {
        val fa = randomUUIDStr()
        val fb = randomUUIDStr()
        val pa = "copy/$fa"
        val pb = "copy/$fb"
        val byteArray = createFile(pb)
        service.copyFile("copy", fa, "copy", fb)
        fileExist(pb) shouldBe true
        fileExist(pa) shouldBe true
        fileContent(pa) shouldBe byteArray
    }

    @Test
    fun `copy file between different bucket`() {
        val fa = randomUUIDStr()
        val fb = randomUUIDStr()
        val pa = "copy/$fa"
        val pb = "copy2/$fb"
        val byteArray = createFile(pb)
        service.copyFile("copy", fa, "copy2", fb)
        fileExist(pb) shouldBe true
        fileExist(pa) shouldBe true
        fileContent(pa) shouldBe byteArray
    }

    @Test
    fun remove() {
        val fa = randomUUIDStr()
        val pa = "copy/$fa"
        val byteArray = createFile(pa)
        service.removeFile("copy", fa)
        fileExist(pa) shouldBe false
    }


    private fun fileExist(path: String): Boolean {
        val p = rootDir + path.trim('/')
        val result = container.execInContainer("ls", p)
        return result.exitCode == 0
    }

    private fun fileContent(path: String): ByteArray {
        val p = rootDir + path.trim('/')
        return container.copyFileFromContainer(p) {
            it.readAllBytes()
        }
    }

    fun createFile(path: String, length: Int = 1000): ByteArray {
        val array = ByteArray(length).apply { Random().nextBytes(this) }
        val p = rootDir + path.trim('/')
        container.copyFileToContainer(Transferable.of(array), p)
        container.execInContainer("chown", "-R", "ftp:ftp", rootDir)
        return array
    }


}