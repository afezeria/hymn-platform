package github.afezeria.hymn.oss.platform

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.platform.PermService
import github.afezeria.hymn.common.util.*
import github.afezeria.hymn.oss.OssCacheKey
import github.afezeria.hymn.oss.StorageService
import github.afezeria.hymn.oss.module.dto.FileRecordDto
import github.afezeria.hymn.oss.module.entity.FileRecord
import github.afezeria.hymn.oss.module.service.FileRecordService
import github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import mu.KLogging
import org.springframework.data.redis.core.RedisTemplate
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * @author afezeria
 */
class OssServiceImpl(
    private val prefix: String,
    private val fileRecordService: FileRecordService,
    private val preSignedHistoryService: PreSignedHistoryService,
    private val dataBaseService: DataBaseService,
    private val permService: PermService,
    private val storageService: StorageService,
    private val redisTemplate: RedisTemplate<String, String>,
) : OssService {

    companion object : KLogging()

    init {
        if (!"([a-z][-a-z0-9]{0,9})?".toRegex().matches(prefix)) {
            throw InnerException("$prefix 不是有效的 bucket 前缀")
        }
    }

    override fun putObject(
        bucket: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String,
        tmp: Boolean,
    ): String {
        return dataBaseService.db().useTransaction {
            bucket.throwIfBucketNameInvalid()
            val b = prefix + bucket
            val fileName = objectName.split('/').last()
            fileName.throwIfFileNameInvalid()
            val path = objectName.replace(
                "$fileName$".toRegex(),
                "${System.currentTimeMillis()}-$fileName"
            )

            val id = fileRecordService.create(
                FileRecordDto(
                    bucket, fileName,
                    contentType = contentType,
                    path = path,
                )
            )
            val size = storageService.putFile(b, path, inputStream, contentType)
            fileRecordService.update(id, mapOf("size" to size.toInt()))
            id
        }
    }

    override fun getObject(bucket: String, objectName: String, fn: (InputStream) -> Unit) {
        bucket.throwIfBucketNameInvalid()
        fileRecordExist(bucket, objectName)
        val b = prefix + bucket
        storageService.getFile(b, objectName, fn)
    }

    override fun moveObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        bucket.throwIfBucketNameInvalid()
        srcBucket.throwIfBucketNameInvalid()
        if (bucket == srcBucket && objectName == srcObjectName) {
            throw BusinessException("源对象和目标对象不能为同一个")
        }
        val fileName = objectName.split('/').last()
        fileName.throwIfFileNameInvalid()
        val record = fileRecordExist(srcBucket, srcObjectName)
        val newObjectName =
            objectName.replace(
                "$fileName$".toRegex(),
                "${System.currentTimeMillis()}-$fileName"
            )
        val b1 = prefix + bucket
        val b2 = prefix + srcBucket
        storageService.moveFile(b1, newObjectName, b2, srcObjectName)
        fileRecordService.update(record.id, mapOf("bucket" to bucket, "path" to newObjectName))
    }

    override fun copyObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ): String {
        bucket.throwIfBucketNameInvalid()
        srcBucket.throwIfBucketNameInvalid()
        if (bucket == srcBucket && objectName == srcObjectName) {
            throw BusinessException("源对象和目标对象不能为同一个")
        }
        val b1 = prefix + bucket
        val b2 = prefix + srcBucket

        val fileName = objectName.split('/').last()
        fileName.throwIfFileNameInvalid()
        val srcRecord = fileRecordExist(srcBucket, srcObjectName)
        val newObjectName =
            objectName.replace("$fileName$".toRegex(), "${System.currentTimeMillis()}-$fileName")
        storageService.copyFile(
            b1, newObjectName, b2, srcObjectName
        )
        return fileRecordService.create(
            FileRecordDto(
                bucket = bucket,
                fileName = fileName,
                contentType = srcRecord.contentType,
                path = newObjectName,
                objectId = null,
                fieldId = null,
                dataId = null,
                size = srcRecord.size,
                remark = srcRecord.remark
            )
        )
    }

    override fun getObjectUrl(bucket: String, objectName: String, expiry: Int): String {
        bucket.throwIfBucketNameInvalid()
        val fileId = fileRecordExist(bucket, objectName).id
        storageService.fileExist(prefix + bucket, objectName, null)

        logger.info("开始获取文件下载链接，文件路径： bucket: ${bucket}, objectName: $objectName")

        return if (storageService.remoteServerSupportHttpAccess()) {
            storageService.getFileUrl(prefix + bucket, objectName, expiry)
        } else {
            val random = randomUUIDStr()
            val result = redisTemplate.opsForValue().setIfAbsent(
                OssCacheKey.preSigned(random),
                fileId,
                expiry.toLong(),
                TimeUnit.SECONDS
            )!!
            if (result) {
                "/module/oss/public/pre-signed/$random"
            } else {
                ""
            }
        }
    }

    override fun removeObject(bucket: String, objectName: String) {
        bucket.throwIfBucketNameInvalid()
        val b = prefix + bucket

        storageService.removeFile(b, objectName)
        fileRecordService.removeByBucketAndPath(bucket, objectName)
    }

    override fun objectExist(bucket: String, objectName: String): Boolean {
        bucket.throwIfBucketNameInvalid()
        return try {
            fileRecordExist(bucket, objectName)
            storageService.fileExist(prefix + bucket, objectName, null)
            true
        } catch (e: BusinessException) {
            false
        }
    }

    override fun getObjectWithPerm(objectId: String, fn: (InputStream) -> Unit) {
        val record = fileRecordService.findById(objectId)
            ?: throw BusinessException("对象不存在".msgById(objectId))
        record.apply {
            if (visibility == "normal" || visibility == "anonymous") {
                storageService.getFile(bucket, path, fn)
                return
            }
            if ((dataId != null && this.objectId != null && this.fieldId != null)
                && permService.hasFieldPerm(this.objectId!!, fieldId!!)
                && permService.hasDataPerm(this.objectId!!, dataId!!)
            ) {
                storageService.getFile(bucket, path, fn)
                return
            }
            throw PermissionDeniedException()
        }
    }

    override fun getObject(objectId: String, fn: (InputStream) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun removeObjectWithPerm(objectId: String) {
        TODO("Not yet implemented")
    }

    override fun removeObject(objectId: String) {
        TODO("Not yet implemented")
    }

    override fun getObjectListByBucket(
        bucket: String,
        pageSize: Int,
        pageNum: Int
    ): List<Pair<String, String>> {
        TODO("Not yet implemented")
    }

    private fun fileRecordExist(bucket: String, objectName: String): FileRecord {
        return fileRecordService.findByBucketAndPath(bucket, objectName)
            ?: throw BusinessException("对象 bucket:$bucket,objectName:$objectName 不存在")
    }
}