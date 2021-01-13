package github.afezeria.hymn.oss

import github.afezeria.hymn.common.KGenericContainer
import github.afezeria.hymn.common.randomUUIDStr
import github.afezeria.hymn.oss.minio.MinioConfig
import github.afezeria.hymn.oss.minio.MinioStorageService
import github.afezeria.hymn.oss.web.controller.SimpleFileController
import io.kotest.matchers.shouldBe
import io.minio.*
import io.minio.errors.ErrorResponseException
import io.mockk.mockk
import mu.KLogging
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.Duration


/**
 * @author afezeria
 */
@Testcontainers
class MinioStorageServiceTest {

    //    private var minioServer: GenericContainer<*>? = null
//    private var minioServerUrl: String? = null


    companion object : KLogging() {
        private const val port = 9000

        private const val ADMIN_ACCESS_KEY = "admin"
        private const val ADMIN_SECRET_KEY = "12345678"

    }

    @Container
    var container = KGenericContainer("minio/minio")
        .withEnv(
            mapOf(
                "MINIO_ACCESS_KEY" to ADMIN_ACCESS_KEY,
                "MINIO_SECRET_KEY" to ADMIN_SECRET_KEY
            )
        )
        .withCommand("server /data")
        .withExposedPorts(port)
        .waitingFor(
            HttpWaitStrategy()
                .forPath("/minio/health/ready")
                .forPort(port)
                .withStartupTimeout(Duration.ofSeconds(10))
        )

    lateinit var minioService: MinioStorageService
    lateinit var fileController: SimpleFileController
    lateinit var minio: MinioClient

    @BeforeEach
    fun setUp() {
        val config = MinioConfig(
            url = "http://${container.host}:${container.firstMappedPort}",
            accessKey = ADMIN_ACCESS_KEY,
            secretKey = ADMIN_SECRET_KEY,
            prefix = null,
            useMinioPreSignedURL = true
        )
        minio = MinioClient.builder()
            .endpoint(config.url)
            .credentials(config.accessKey, config.secretKey)
            .build()
        fileController = mockk()
        minioService = MinioStorageService(config, fileController)
    }


    @Test
    fun `upload test`() {
        bucketExists("test") shouldBe false
        val str = "hello"
        minioService.putFile(
            "test",
            "abc.text",
            ByteArrayInputStream(str.toByteArray()),
            "text/plain"
        )
        bucketExists("test") shouldBe true
        minio.getObject(
            GetObjectArgs.builder()
                .bucket("test")
                .`object`("abc.text")
                .build()
        ).apply {
            println(this.headers())
            readAllBytes().decodeToString() shouldBe str
            this.headers().get("Content-Type") shouldBe "text/plain"
        }
    }

    @Test
    fun `download test`() {
        createBucket("test")
        minio.putObject(
            PutObjectArgs.builder()
                .bucket("test")
                .`object`("abc.text")
                .stream(
                    ByteArrayInputStream("abc".toByteArray()), -1,
                    10485760
                )
                .build()
        )
        val stream = ByteArrayOutputStream()
        minioService.getFile("test", "abc.text") {
            IOUtils.copy(it, stream)
        }
        stream.toByteArray().decodeToString() shouldBe "abc"
    }

    @Test
    fun `move file in the same bucket`() {
        val abc = createBucket("abc")
        val srcFile = "abc.txt"
        val newFile = "def.txt"
        val str = createFile(abc, "abc.txt")
        minioService.moveFile(abc, newFile, abc, srcFile)
        getFile(abc, newFile).apply {
            readAllBytes().decodeToString() shouldBe str
        }
        fileExists(abc, srcFile) shouldBe false
    }

    private fun createFile(bucket: String, fileName: String): String {
        val text = randomUUIDStr()
        minio.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(fileName)
                .stream(
                    ByteArrayInputStream(text.toByteArray()), -1,
                    10485760
                )
                .build()
        )
        return text
    }

    private fun getFile(bucket: String, fileName: String): GetObjectResponse {
        return minio.getObject(
            GetObjectArgs.builder()
                .bucket(bucket).`object`(fileName).build()
        )
    }

    private fun fileExists(bucket: String, fileName: String): Boolean {
        try {
            minio.statObject(
                StatObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(fileName)
                    .build()
            )
            return true
        } catch (e: ErrorResponseException) {
            if (e.response().code() == 404) {
                return false
            }
            throw e
        }
    }

    private fun bucketExists(bucket: String): Boolean {
        return minio.bucketExists(
            BucketExistsArgs.builder()
                .bucket(bucket)
                .build()
        )
    }

    private fun createBucket(bucket: String): String {
        val isExist: Boolean =
            minio.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build()
            )
        if (!isExist) {
            minio.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build()
            )
        }
        return bucket
    }
}