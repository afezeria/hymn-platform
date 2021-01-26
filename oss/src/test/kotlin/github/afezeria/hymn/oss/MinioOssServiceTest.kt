package github.afezeria.hymn.oss

import github.afezeria.hymn.common.KGenericContainer
import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.common.util.randomUUIDStr
import github.afezeria.hymn.oss.minio.MinioConfig
import github.afezeria.hymn.oss.minio.MinioOssService
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.minio.*
import io.minio.errors.ErrorResponseException
import mu.KLogging
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.Duration


/**
 * @author afezeria
 */
@Testcontainers
class MinioOssServiceTest {

    //    private var minioServer: GenericContainer<*>? = null
//    private var minioServerUrl: String? = null


    companion object : KLogging() {
        private const val port = 9000

        private const val ADMIN_ACCESS_KEY = "admin"
        private const val ADMIN_SECRET_KEY = "12345678"

        val lower = sequence {
            var current = 'a'
            while (true) {
                yield(current)
                if (current == 'z') current = 'a'
                else current += 1
            }
        }
        lateinit var container: KGenericContainer

        lateinit var service: StorageService
        lateinit var minio: MinioClient

        val objectList = mutableListOf<Pair<String, String>>()

        @BeforeAll
        @JvmStatic
        fun before() {
            container = KGenericContainer("minio/minio")
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
                        .withStartupTimeout(Duration.ofSeconds(20))
                )
            container.start()
            val config = MinioConfig().apply {
                url = "http://${container.host}:${container.firstMappedPort}"
                accessKey = ADMIN_ACCESS_KEY
                secretKey = ADMIN_SECRET_KEY
                useMinioPreSignedURL = true
            }
            minio = MinioClient.builder()
                .endpoint(config.url)
                .credentials(config.accessKey, config.secretKey)
                .build()
            service = MinioOssService(config)
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            container.use { }
        }
    }

    @AfterEach
    fun afterEach() {
        for ((b, f) in objectList) {
            minio.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(b)
                    .`object`(f)
                    .build()
            )
        }
        for (b in objectList.map { it.first }.toSet()) {
            minio.removeBucket(
                RemoveBucketArgs.builder()
                    .bucket(b)
                    .build()
            )
        }
        objectList.clear()
    }


    @Test
    fun upload() {
        bucketExists("test") shouldBe false
        val bucket = createBucket()
        val str = "hello"
        val file = "abc.txt"
        service.putFile(
            bucket,
            file,
            ByteArrayInputStream(str.toByteArray()),
            "text/plain"
        )
        objectList.add(bucket to file)
        bucketExists(bucket) shouldBe true
        minio.getObject(
            GetObjectArgs.builder()
                .bucket(bucket)
                .`object`(file)
                .build()
        ).apply {
            readAllBytes().decodeToString() shouldBe str
            this.headers().get("Content-Type") shouldBe "text/plain"
        }
    }

    @Test
    fun `upload with path`() {
        bucketExists("test") shouldBe false
        val bucket = createBucket()
        val str = "hello"
        val file = "2020/01/01/abc.text"
        service.putFile(
            bucket,
            file,
            ByteArrayInputStream(str.toByteArray()),
            "text/plain"
        )
        objectList.add(bucket to file)
        bucketExists(bucket) shouldBe true
        minio.getObject(
            GetObjectArgs.builder()
                .bucket(bucket)
                .`object`(file)
                .build()
        ).apply {
            readAllBytes().decodeToString() shouldBe str
            this.headers().get("Content-Type") shouldBe "text/plain"
        }
    }

    @Test
    fun download() {
        val bucket = createBucket()
        val file = "abc.txt"
        minio.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(file)
                .stream(
                    ByteArrayInputStream("abc".toByteArray()), -1,
                    10485760
                )
                .build()
        )
        objectList.add(bucket to file)
        val stream = ByteArrayOutputStream()
        service.getFile(bucket, file) {
            IOUtils.copy(it, stream)
        }
        stream.toByteArray().decodeToString() shouldBe "abc"
    }

    @Test
    fun `move file in the same bucket`() {
        val bucket = createBucket()
        val srcFile = "abc.txt"
        val newFile = "def.txt"
        val str = createFile(bucket, srcFile)
        objectList.add(bucket to srcFile)
        objectList.add(bucket to newFile)
        service.moveFile(bucket, newFile, bucket, srcFile)
        getFile(bucket, newFile).apply {
            readAllBytes().decodeToString() shouldBe str
        }
        fileExists(bucket, srcFile) shouldBe false
    }

    @Test
    fun `move file between different bucket`() {
        val bucket = createBucket()
        val srcBucket = createBucket()
        val srcFile = "abc.txt"
        val newFile = "def.txt"
        val str = createFile(srcBucket, srcFile)
        service.moveFile(bucket, newFile, srcBucket, srcFile)
        objectList.add(bucket to newFile)
        objectList.add(srcBucket to srcFile)
        getFile(bucket, newFile).apply {
            readAllBytes().decodeToString() shouldBe str
        }
        fileExists(srcBucket, srcFile) shouldBe false
    }

    @Test
    fun `copy file in the same bucket`() {
        val bucket = createBucket()
        val srcFile = "abc.txt"
        val newFile = "def.txt"
        val str = createFile(bucket, srcFile)
        service.copyFile(bucket, newFile, bucket, srcFile)
        objectList.add(bucket to srcFile)
        objectList.add(bucket to newFile)
        getFile(bucket, newFile).apply {
            readAllBytes().decodeToString() shouldBe str
        }
        fileExists(bucket, srcFile) shouldBe true
    }

    @Test
    fun `copy file between different bucket`() {
        val bucket = createBucket()
        val srcBucket = createBucket()
        val srcFile = "abc.txt"
        val newFile = "def.txt"
        val str = createFile(srcBucket, srcFile)
        service.copyFile(bucket, newFile, srcBucket, srcFile)
        objectList.add(bucket to newFile)
        objectList.add(srcBucket to srcFile)
        getFile(bucket, newFile).apply {
            readAllBytes().decodeToString() shouldBe str
        }
        fileExists(srcBucket, srcFile) shouldBe true
    }

    @Test
    fun remove() {
        val bucket = createBucket()
        val file = "tt"
        createFile(bucket, file)
        service.removeFile(bucket, file)
        objectList.add(bucket to file)
        fileExists(bucket, file) shouldBe false
    }

    @Test
    fun fileExist() {
        val bucket = createBucket()
        val file = "abc"
        createFile(bucket, file)
        shouldNotThrow<BusinessException> {
            service.fileExist(bucket, file)
        }
        objectList.add(bucket to file)
        shouldThrow<BusinessException> {
            service.getFile("abc242", "abc", {})
        }.apply {
            message shouldBe "文件不存在"
        }

    }

    @Test
    fun `get a file that does not exist`() {
        shouldThrow<BusinessException> {
            service.getFile("abc", "abc", {})
        }.apply {
            message shouldBe "文件不存在"
        }
    }

    @Test
    fun `move nonexistent files`() {
        shouldThrow<BusinessException> {
            service.moveFile("abc", "abc", "bcd", "bcd")
        }.apply {
            message shouldBe "文件不存在"
        }
    }

    @Test
    fun `copy nonexistent files`() {
        shouldThrow<BusinessException> {
            service.copyFile("abc", "abc", "bcd", "bcd")
        }.apply {
            message shouldBe "文件不存在"
        }
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

    private fun createBucket(): String {
        val bucket = lower.take(4).joinToString("")
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