package github.afezeria.hymn.oss

import github.afezeria.hymn.common.KGenericContainer
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.oss.ftp.FTPClientFactory
import github.afezeria.hymn.oss.ftp.FTPConfig
import github.afezeria.hymn.oss.ftp.FTPOssService
import github.afezeria.hymn.oss.web.controller.SimpleFileController
import io.mockk.mockk
import mu.KLogging
import org.apache.commons.net.ftp.FTPClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.ByteArrayInputStream
import java.time.Duration

/**
 * @author afezeria
 */
@Testcontainers
class FTPOssServiceTest {
    companion object : KLogging() {
        const val USERNAME = "admin"
        const val PASSWORD = "123456"
    }

    @Container
    var container = KGenericContainer("fauria/vsftpd")
        .withEnv(
            mapOf(
                "FTP_PASS" to PASSWORD,
            )
        )
        .withExposedPorts(21)
        .waitingFor(
            HostPortWaitStrategy()
                .withStartupTimeout(Duration.ofSeconds(10))
        )
    lateinit var config: FTPConfig
    lateinit var ftp: FTPClient
    lateinit var service: OssService
    lateinit var fileController: SimpleFileController

    @BeforeEach
    fun beforeEach() {
        config = FTPConfig(
            host = container.host,
            port = container.firstMappedPort,
            path = null,
            username = USERNAME,
            password = PASSWORD,
            connectTimeout = 0,
            bufferSize = 0,
            prefix = null
        )
        ftp = FTPClientFactory(config).create()
        fileController = mockk()
        service = FTPOssService(config, fileController)
    }

    @Test
    fun abc() {
        val str = "abc"
        service.putObject(
            "abc",
            "abc.txt",
            ByteArrayInputStream(str.toByteArray()),
            "text/plain"
        )
        println()
    }
}