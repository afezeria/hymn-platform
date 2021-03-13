package com.github.afezeria.hymn.oss.platform

import com.github.afezeria.hymn.common.*
import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.exception.PermissionDeniedException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.platform.OssService
import com.github.afezeria.hymn.common.platform.PermService
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.common.testconfiguration.RedisTestConfig
import com.github.afezeria.hymn.common.util.execute
import com.github.afezeria.hymn.common.util.randomUUIDStr
import com.github.afezeria.hymn.oss.OssConfigProperties
import com.github.afezeria.hymn.oss.OssTestConfiguration
import com.github.afezeria.hymn.oss.StorageService
import com.github.afezeria.hymn.oss.module.entity.FileRecord
import com.github.afezeria.hymn.oss.module.service.FileRecordService
import com.github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import com.github.afezeria.hymn.oss.web.controller.PreSignedUrlController
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
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
        databaseService.primary().useConnection {
            it.execute("delete from hymn.oss_file_record")
            it.execute("delete from hymn.oss_pre_signed_history")
        }
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

    @Nested
    inner class GetObjectWithPerm {
        @BeforeEach
        fun before() {
            justRun { storageService.getFile(any(), any(), any()) }
            every { fileRecordService.findById(any()) } returns defaultRecord
        }

        @Test
        fun admin() {
            val bucketSlot = slot<String>()
            justRun { storageService.getFile(capture(bucketSlot), any(), any()) }
            shouldNotThrow<PermissionDeniedException> {
                service.getObjectWithPerm(defaultRecord.id, {})
            }
            bucketSlot.captured shouldBe "${ossConfigProperties.prefix}$defaultBucket"
            verify(exactly = 1) { storageService.getFile(any(), any(), any()) }
        }

        @Test
        fun `anonymous users get files visible to normal users`() {
            every { Session.getInstance() } returns anonymousSession
            shouldThrow<PermissionDeniedException> {
                service.getObjectWithPerm(defaultRecord.id, {})
            }
        }

        @Test
        fun `anonymous users get files visible to anonymous users`() {
            every { Session.getInstance() } returns anonymousSession
            defaultRecord.visibility = "anonymous"
            shouldNotThrow<PermissionDeniedException> {
                service.getObjectWithPerm(defaultRecord.id, {})
            }
        }

        @Test
        fun `normal users get files visible to anonymous users`() {
            defaultRecord.visibility = "anonymous"
            every { Session.getInstance() } returns userSession
            shouldNotThrow<PermissionDeniedException> {
                service.getObjectWithPerm(defaultRecord.id, {})
            }

        }

        @Test
        fun `normal users get files visible to normal users`() {
            defaultRecord.visibility = "normal"
            every { Session.getInstance() } returns userSession
            shouldNotThrow<PermissionDeniedException> {
                service.getObjectWithPerm(defaultRecord.id, {})
            }
            verify(exactly = 0) {
                permService.hasObjectPerm(any(), any(), query = true)
                permService.hasFieldPerm(any(), any(), read = true)
                permService.hasDataPerm(any(), any(), read = true)
            }
        }

        @Test
        fun `normal users get files visible to admin users`() {
            every { Session.getInstance() } returns userSession
            shouldThrow<PermissionDeniedException> {
                service.getObjectWithPerm(defaultRecord.id, {})
            }
            verify(exactly = 0) {
                permService.hasObjectPerm(any(), any(), query = true)
                permService.hasFieldPerm(any(), any(), read = true)
                permService.hasDataPerm(any(), any(), read = true)
            }
        }

        @Test
        fun `normal users get attachments belonging to data`() {
            every { Session.getInstance() } returns userSession
            defaultRecord.objectId = randomUUIDStr()
            defaultRecord.fieldId = randomUUIDStr()
            defaultRecord.dataId = randomUUIDStr()
            shouldNotThrow<PermissionDeniedException> {
                service.getObjectWithPerm(defaultRecord.id, {})
            }
            verify(exactly = 1) {
                permService.hasObjectPerm(any(), query = true)
                permService.hasFieldPerm(any(), any(), read = true)
                permService.hasDataPerm(any(), any(), read = true)
            }
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

    @Nested
    inner class GetObjectUrl {
        val url = "abc"

        @BeforeEach
        fun before() {
            every { fileRecordService.findByBucketAndPath(any(), any()) } returns defaultRecord
            justRun { storageService.fileExist(any(), any(), any()) }
            every { storageService.remoteServerSupportHttpAccess() } returns false
            every { preSignedUrlController.generatePreSignedObjectUrl(any(), any()) } returns url
            every { preSignedHistoryService.create(any()) } returns randomUUIDStr()
        }

        @Test
        fun admin() {
            service.getObjectUrl(defaultBucket, defaultPath) shouldBe url
        }

        @Test
        fun `anonymous users create object url`() {
            every { Session.getInstance() } returns anonymousSession
//        匿名帐号创建url
            shouldThrow<PermissionDeniedException> {
                service.getObjectUrl(defaultBucket, defaultPath)
            }
        }

        @Test
        fun `normal users create public object url`() {
            every { Session.getInstance() } returns userSession
//        普通用户对公共对象创建url
            shouldThrow<PermissionDeniedException> {
                service.getObjectUrl(defaultBucket, defaultPath)
            }
        }

        @Test
        fun `normal users create attachment url`() {
            every { Session.getInstance() } returns userSession
            defaultRecord.apply {
                objectId = randomUUIDStr()
                fieldId = randomUUIDStr()
                dataId = randomUUIDStr()
            }
            shouldNotThrow<PermissionDeniedException> {
                service.getObjectUrl(defaultBucket, defaultPath)
            }
            verify(exactly = 1) {
                permService.hasObjectPerm(any(), query = true)
                permService.hasFieldPerm(any(), any(), read = true)
                permService.hasDataPerm(any(), any(), read = true)
            }

        }

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

    @Nested
    inner class RemoveObjectWithPerm {
        @BeforeEach
        fun before() {
            every { fileRecordService.findById(any()) } returns defaultRecord
            every { fileRecordService.removeById(any()) } returns 1
            justRun { storageService.removeFile(any(), any()) }
        }

        @Test
        fun admin() {
            val bucketSlot = slot<String>()
            justRun { storageService.removeFile(capture(bucketSlot), any()) }
            service.removeObjectWithPerm(defaultRecord.id) shouldBe 1
            bucketSlot.captured shouldBe "${ossConfigProperties.prefix}${defaultRecord.bucket}"
        }

        @Test
        fun `anonymous users remove object`() {
            every { Session.getInstance() } returns anonymousSession
            shouldThrow<PermissionDeniedException> {
                service.removeObjectWithPerm(defaultRecord.id)
            }
        }

        @Test
        fun `normal users remove public object`() {
            every { Session.getInstance() } returns userSession
            shouldThrow<PermissionDeniedException> {
                service.removeObjectWithPerm(defaultRecord.id)
            }

        }

        @Test
        fun `normal users remove data attachments`() {
            every { Session.getInstance() } returns userSession
            defaultRecord.apply {
                objectId = randomUUIDStr()
                fieldId = randomUUIDStr()
                dataId = randomUUIDStr()
            }
            shouldNotThrow<PermissionDeniedException> {
                service.removeObjectWithPerm(defaultRecord.id)
            }
            verify(exactly = 1) {
                permService.hasObjectPerm(any(), query = true, update = true)
                permService.hasFieldPerm(any(), any(), read = true, edit = true)
                permService.hasDataPerm(any(), any(), read = true, update = true)
            }

        }

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