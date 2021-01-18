package github.afezeria.hymn.oss

import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.common.util.InnerException
import github.afezeria.hymn.oss.local.LocalConfig
import github.afezeria.hymn.oss.local.LocalOssService
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.*
import mu.KLogging
import org.junit.jupiter.api.*
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

/**
 * @author afezeria
 */
class AbstractOssServiceTest {
    companion object : KLogging() {
        lateinit var config: LocalConfig
        lateinit var localOssService: LocalOssService
        lateinit var rootDir: String

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            rootDir = System.getProperty("user.dir") + "/test-dir/"
            config = LocalConfig(
                rootDir = rootDir, prefix = "test-"
            )
            localOssService = LocalOssService(mockk(), config)
        }

        @AfterAll
        @JvmStatic
        fun after() {
            Files.walk(Path.of(rootDir)).use { walk ->
                walk.sorted(Comparator.reverseOrder())
                    .map { obj: Path -> obj.toFile() }
                    .peek { println("delete $it") }
                    .forEach(File::delete)
            }
        }
    }

    lateinit var service: AbstractOssService

    @BeforeEach
    fun before() {
        service = spyk(localOssService, recordPrivateCalls = true)
    }

    @AfterEach
    fun afterEach() {
        clearMocks(service)
    }

    @Test
    fun `add prefix automatically`() {
        assertSoftly {

            val slot1 = slot<String>()
            val slot2 = slot<String>()
            justRun {
                service.putFile(
                    capture(slot1),
                    objectName = any(),
                    inputStream = any(),
                    contentType = any(),
                )
            }
            service.putObject(
                "img",
                "abc",
                ByteArrayInputStream(ByteArray(1)),
                "text/plain"
            )
            slot1.captured shouldBe "test-img"

            justRun {
                service.getFile(bucket = capture(slot1), objectName = any(), fn = any())
            }
            service.getObject("archive", "cc.txt", {})
            slot1.captured shouldBe "test-archive"

            justRun {
                service.moveFile(
                    bucket = capture(slot1),
                    objectName = any(),
                    srcBucket = capture(slot2),
                    srcObjectName = any()
                )
            }
            service.moveObject("bb1", "abc", "bb2", "abc")
            slot1.captured shouldBe "test-bb1"
            slot2.captured shouldBe "test-bb2"

            justRun {
                service.copyFile(
                    bucket = capture(slot1),
                    objectName = any(),
                    srcBucket = capture(slot2),
                    srcObjectName = any()
                )
            }
            service.copyObject("bb1", "abc", "bb2", "abc")
            slot1.captured shouldBe "test-bb1"
            slot2.captured shouldBe "test-bb2"

            every {
                service.getFileUrl(bucket = capture(slot1), objectName = any(), expiry = any())
            } returns ""
            justRun { service.fileExist(any(), any(), any()) }
            every { service.remoteServerSupportHttpAccess() } returns true
            service.getObjectUrl("bbc", "aa")
            slot1.captured shouldBe "test-bbc"

            justRun {
                service.removeFile(capture(slot1), any())
            }
            service.removeObject("tmp", "bb.txt")
            slot1.captured shouldBe "test-tmp"
        }
    }

    @Test
    fun `throw when bucket invalid`() {
        shouldThrow<InnerException> {
            service.putObject(
                "a_b",
                "abc",
                ByteArrayInputStream(ByteArray(1)),
                "text/plain"
            )
        }.apply {
            message shouldBe "a_b 不是有效的bucket名称"
        }
    }

    @Test
    fun `throw when invalid prefix of bucket `() {
        shouldThrow<InnerException> {
            LocalOssService(
                mockk(),
                LocalConfig(
                    rootDir = rootDir, prefix = "0"
                )
            )
        }.apply {
            message shouldBe "0 不是有效的 bucket 前缀"
        }
    }

    @Test
    fun `source and target object paths cannot be the same`() {

        shouldThrow<BusinessException> {
            service.moveObject(
                "abc",
                "txt",
                "abc",
                "txt",
            )
        }.apply {
            message shouldBe "源对象和目标对象不能为同一个"
        }
        shouldThrow<BusinessException> {
            service.copyObject(
                "abc",
                "txt",
                "abc",
                "txt",
            )
        }.apply {
            message shouldBe "源对象和目标对象不能为同一个"
        }
    }

}