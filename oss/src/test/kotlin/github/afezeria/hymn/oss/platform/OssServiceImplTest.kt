package github.afezeria.hymn.oss.platform

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.exception.PermissionDeniedException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.platform.PermService
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.randomUUIDStr
import github.afezeria.hymn.oss.OssConfigProperties
import github.afezeria.hymn.oss.OssTestConfiguration
import github.afezeria.hymn.oss.StorageService
import github.afezeria.hymn.oss.module.entity.FileRecord
import github.afezeria.hymn.oss.module.service.FileRecordService
import github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import github.afezeria.hymn.oss.web.controller.PreSignedUrlController
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.ByteArrayInputStream
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [TestApplication::class, RedisTestConfig::class, OssTestConfiguration::class],
)
internal class OssServiceImplTest {


    @MockkBean
    lateinit var storageService: StorageService

    @MockkBean
    lateinit var preSignedUrlController: PreSignedUrlController

    @SpykBean
    lateinit var fileRecordService: FileRecordService

    @SpykBean
    lateinit var preSignedHistoryService: PreSignedHistoryService

    @Autowired
    lateinit var databaseService: DatabaseService

    @Autowired
    lateinit var ossConfigProperties: OssConfigProperties

    @Autowired
    lateinit var permService: PermService

    @Autowired
    lateinit var service: OssService

    val defaultBucket = "other"
    val defaultPath = "2020/01/01/abc.txt"
    val defaultPathRegex = "2020/01/01/.*?abc.txt".toRegex()
    val defaultFilename = "abc.txt"
    val defaultContentType = "application/octet-stream"
    val defaultContent = "abc".toByteArray()
    val defaultRecord = FileRecord(
        bucket = defaultBucket,
        fileName = defaultFilename,
        contentType = defaultContentType,
        path = defaultPath,
        objectId = null,
        fieldId = null,
        dataId = null,
        size = 3,
        tmp = null,
        visibility = null,
        remark = null
    ).apply {
        id = randomUUIDStr()
        createBy = DEFAULT_ACCOUNT_NAME
        createById = DEFAULT_ACCOUNT_ID
        createDate = LocalDateTime.now()
        modifyBy = DEFAULT_ACCOUNT_NAME
        modifyById = DEFAULT_ACCOUNT_ID
        modifyDate = LocalDateTime.now()
    }

    @BeforeEach
    fun before() {
        mockkObject(Session.Companion)
        every { Session.getInstance() } returns adminSession
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
        every { permService.hasFieldPerm(any(), any()) } returns true
        every { permService.hasFunctionPerm(any(), any()) } returns true
        every { permService.hasDataPerm(any(), any()) } returns true
        every { permService.hasObjectPerm(any()) } returns true

        databaseService.primary().useConnection {
            it.execute("delete from hymn.oss_file_record")
            it.execute("delete from hymn.oss_pre_signed_history")
        }
    }

    @Test
    fun getPrefix() {
        OssServiceImpl(OssConfigProperties(prefix = "abc")).prefix shouldBe "abc"
    }

    @Test
    fun putObject() {
        every { storageService.putFile(any(), any(), any(), any()) } returns 3
        val id = service.putObject(
            defaultBucket, defaultPath, ByteArrayInputStream(defaultContent),
            defaultContentType
        )
        fileRecordService.findById(id)!!.apply {
            assertSoftly {

                bucket shouldBe defaultBucket
                fileName shouldBe defaultFilename
                path shouldMatch defaultPathRegex
                contentType shouldBe defaultContentType
                size shouldBe 3
            }
        }
    }

    @Test
    fun getObject() {
        val slot = slot<String>()
        justRun { storageService.getFile(capture(slot), any(), any()) }
        every { fileRecordService.findByBucketAndPath(any(), any()) } returns defaultRecord
        service.getObject(defaultBucket, defaultPath, {})
        verify(exactly = 1) { storageService.getFile(any(), any(), any()) }
        slot.captured shouldBe "${ossConfigProperties.prefix}$defaultBucket"
    }

    @Test
    fun getObjectById() {
        justRun { storageService.getFile(any(), any(), any()) }
        every { fileRecordService.findById(any()) } returns defaultRecord
        service.getObject(defaultRecord.id, {})
        verify(exactly = 1) { storageService.getFile(any(), any(), any()) }
    }

    @Test
    fun getObjectWithPerm() {
        val bucketSlot = slot<String>()
        justRun { storageService.getFile(capture(bucketSlot), any(), any()) }
        every { fileRecordService.findById(any()) } returns defaultRecord
        shouldNotThrow<PermissionDeniedException> {
            service.getObjectWithPerm(defaultRecord.id, {})
        }
        bucketSlot.captured shouldBe "${ossConfigProperties.prefix}$defaultBucket"
        verify(exactly = 1) { storageService.getFile(any(), any(), any()) }

        every { Session.getInstance() } returns anonymousSession
//        匿名用户获取普通文件
        shouldThrow<PermissionDeniedException> {
            service.getObjectWithPerm(defaultRecord.id, {})
        }
//        匿名用户获取可见性为anonymous的文件
        defaultRecord.visibility = "anonymous"
        shouldNotThrow<PermissionDeniedException> {
            service.getObjectWithPerm(defaultRecord.id, {})
        }

//        普通用户
        defaultRecord.visibility = null
        every { Session.getInstance() } returns userSession
        shouldThrow<PermissionDeniedException> {
            service.getObjectWithPerm(defaultRecord.id, {})
        }
        verify(exactly = 0) { permService.hasFieldPerm(any(), any()) }

        defaultRecord.objectId = randomUUIDStr()
        defaultRecord.fieldId = randomUUIDStr()
        defaultRecord.dataId = randomUUIDStr()
        shouldNotThrow<PermissionDeniedException> {
            service.getObjectWithPerm(defaultRecord.id, {})
        }
        verify(exactly = 1) {
            permService.hasFieldPerm(any(), any())
            permService.hasDataPerm(any(), any())
        }

        every { permService.hasFieldPerm(any(), any()) } returns false
        shouldThrow<PermissionDeniedException> {
            service.getObjectWithPerm(defaultRecord.id, {})
        }

    }

    @Test
    fun moveObject() {
        val newBucket = "a$defaultBucket"
        val newPath = "/abc/$defaultPath"
        every { fileRecordService.findByBucketAndPath(any(), any()) } returns defaultRecord
        val newBucketSlot = slot<String>()
        val bucketSlot = slot<String>()
        val newPathSlot = slot<String>()
        justRun {
            storageService.moveFile(
                capture(newBucketSlot),
                capture(newPathSlot),
                capture(bucketSlot),
                any()
            )
        }

        every { fileRecordService.findById(any()) } returns defaultRecord
        every { fileRecordService.update(any(), any<Map<String, Any?>>()) } returns 1
        service.moveObject(newBucket, newPath, defaultBucket, defaultPath)
        verify(exactly = 1) { fileRecordService.update(any(), any<Map<String, Any?>>()) }
        newBucketSlot.captured shouldBe "${ossConfigProperties.prefix}$newBucket"
        newPathSlot.captured shouldMatch "/abc/${defaultPathRegex.pattern}".toRegex()
        bucketSlot.captured shouldBe "${ossConfigProperties.prefix}$defaultBucket"
    }

    @Test
    fun copyObject() {
        val newBucket = "a$defaultBucket"
        val newPath = "/abc/$defaultPath"
        every { fileRecordService.findByBucketAndPath(any(), any()) } returns defaultRecord
        val newBucketSlot = slot<String>()
        val bucketSlot = slot<String>()
        val newPathSlot = slot<String>()
        justRun {
            storageService.copyFile(
                capture(newBucketSlot),
                capture(newPathSlot),
                capture(bucketSlot),
                any()
            )
        }
        every { fileRecordService.findById(any()) } returns defaultRecord
        val newId = randomUUIDStr()
        every { fileRecordService.create(any()) } returns newId

        service.copyObject(newBucket, newPath, defaultBucket, defaultPath)

        verify(exactly = 1) { fileRecordService.create(any()) }
        newBucketSlot.captured shouldBe "${ossConfigProperties.prefix}$newBucket"
        newPathSlot.captured shouldMatch "/abc/${defaultPathRegex.pattern}".toRegex()
        bucketSlot.captured shouldBe "${ossConfigProperties.prefix}$defaultBucket"
    }

    @Test
    fun getObjectUrl() {
        every { fileRecordService.findByBucketAndPath(any(), any()) } returns defaultRecord
        justRun { storageService.fileExist(any(), any(), any()) }
        every { storageService.remoteServerSupportHttpAccess() } returns false
        val url = "abc"
        every { preSignedUrlController.generatePreSignedObjectUrl(any(), any()) } returns url
        every { preSignedHistoryService.create(any()) } returns randomUUIDStr()
        service.getObjectUrl(defaultBucket, defaultPath) shouldBe url

        every { Session.getInstance() } returns anonymousSession
//        匿名帐号创建url
        shouldThrow<PermissionDeniedException> {
            service.getObjectUrl(defaultBucket, defaultPath)
        }

        every { Session.getInstance() } returns userSession
//        普通用户对公共对象创建url
        shouldThrow<PermissionDeniedException> {
            service.getObjectUrl(defaultBucket, defaultPath)
        }
        defaultRecord.apply {
            objectId = randomUUIDStr()
            fieldId = randomUUIDStr()
            dataId = randomUUIDStr()
        }
//        普通用户对数据专属文件创建url
        shouldNotThrow<PermissionDeniedException> {
            service.getObjectUrl(defaultBucket, defaultPath)
        }
        verify(exactly = 1) { permService.hasFieldPerm(any(), any()) }
        verify(exactly = 1) { permService.hasDataPerm(any(), any()) }

    }

    @Test
    fun removeObject() {
        val storageBucketSlot = slot<String>()
        val recordBucketSlot = slot<String>()
        justRun { storageService.removeFile(capture(storageBucketSlot), any()) }
        every {
            fileRecordService.removeByBucketAndPath(
                capture(recordBucketSlot),
                any()
            )
        } returns 1
        service.removeObject(defaultBucket, defaultPath)
        storageBucketSlot.captured shouldBe "${ossConfigProperties.prefix}$defaultBucket"
        recordBucketSlot.captured shouldBe defaultBucket
    }

    @Test
    fun objectExist() {
        every { fileRecordService.findByBucketAndPath(any(), any()) } returns defaultRecord
        justRun { storageService.fileExist(any(), any(), any()) }
        service.objectExist(defaultBucket, defaultPath) shouldBe true

        every { fileRecordService.findByBucketAndPath(any(), any()) } returns null
        service.objectExist(defaultBucket, defaultPath) shouldBe false

        every { fileRecordService.findByBucketAndPath(any(), any()) } returns defaultRecord
        every { storageService.fileExist(any(), any(), any()) } throws BusinessException("文件不存在")
        service.objectExist(defaultBucket, defaultPath) shouldBe false
    }

    @Test
    fun getObjectInfoById() {
        val slot = slot<String>()
        every { fileRecordService.findById(capture(slot)) } returns null
        service.getObjectInfoById("abc") shouldBe null
        verify(exactly = 1) { fileRecordService.findById("abc") }
        clearMocks(fileRecordService)
        val id = "id"
        val createById = "create"
        val createDate = LocalDateTime.now()
        val bucket = "other"
        val fileName = "abc.txt"
        val path = "2020/01/01/abc.txt"
        val contentType = "application/octet-stream"
        val size = 3

        every { fileRecordService.findById(any()) } returns FileRecord(
            bucket = bucket,
            fileName = fileName,
            contentType = contentType,
            path = path,
            objectId = null,
            fieldId = null,
            dataId = null,
            size = size,
            tmp = null,
            visibility = null,
            remark = null
        ).apply {
            this.id = id
            this.createById = createById
            this.createDate = createDate
        }
        val info = service.getObjectInfoById("abc")
        verify(exactly = 1) { fileRecordService.findById(any()) }
        assertSoftly {
            info shouldNotBe null
            info?.let {
                it.bucket shouldBe bucket
                it.fileName shouldBe fileName
                it.path shouldBe path
                it.contentType shouldBe contentType
                it.size shouldBe size
                it.recordId shouldBe id
                it.createById shouldBe createById
                it.createDate shouldBe createDate
            }
        }
    }

    @Test
    fun removeObjectWithPerm() {
        val bucketSlot = slot<String>()
        every { fileRecordService.findById(any()) } returns defaultRecord
        every { fileRecordService.removeById(any()) } returns 1
        justRun { storageService.removeFile(capture(bucketSlot), any()) }
        service.removeObjectWithPerm(defaultRecord.id) shouldBe 1
        bucketSlot.captured shouldBe "${ossConfigProperties.prefix}${defaultRecord.bucket}"


        every { Session.getInstance() } returns anonymousSession
//        匿名用户删除
        shouldThrow<PermissionDeniedException> {
            service.removeObjectWithPerm(defaultRecord.id)
        }

        every { Session.getInstance() } returns userSession
//        普通用户删除公共对象
        shouldThrow<PermissionDeniedException> {
            service.removeObjectWithPerm(defaultRecord.id)
        }
        defaultRecord.apply {
            objectId = randomUUIDStr()
            fieldId = randomUUIDStr()
            dataId = randomUUIDStr()
        }
//        普通用户删除属于个人数据的文件
        shouldNotThrow<PermissionDeniedException> {
            service.removeObjectWithPerm(defaultRecord.id)
        }
        verify(exactly = 1) { permService.hasFieldPerm(any(), any()) }
        verify(exactly = 1) { permService.hasDataPerm(any(), any()) }


    }

    @Test
    fun removeObjectById() {
        val bucketSlot = slot<String>()
        every { fileRecordService.findById(any()) } returns defaultRecord
        justRun { storageService.removeFile(capture(bucketSlot), any()) }
        every { fileRecordService.removeById(any()) } returns 1
        service.removeObject(defaultRecord.id)
        bucketSlot.captured shouldBe "${ossConfigProperties.prefix}${defaultRecord.bucket}"
        verify(exactly = 1) { fileRecordService.findById(any()) }
        verify(exactly = 1) { storageService.removeFile(capture(bucketSlot), any()) }
        verify(exactly = 1) { fileRecordService.removeById(any()) }
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

            message shouldBe "源对象路径和目标对象路径不能为同一个"
        }
        shouldThrow<BusinessException> {
            service.copyObject(
                "abc",
                "txt",
                "abc",
                "txt",
            )
        }.apply {
            message shouldBe "源对象路径和目标对象路径不能为同一个"
        }
    }

    @Test
    fun `invalid prefix`() {

        shouldThrow<InnerException> {
            OssServiceImpl(
                OssConfigProperties(
                    prefix = "0"
                ),
            )
        }.apply {
            message shouldBe "0 不是有效的 bucket 前缀"
        }
    }
}