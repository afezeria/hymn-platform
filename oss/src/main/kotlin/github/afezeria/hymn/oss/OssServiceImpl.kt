package github.afezeria.hymn.oss

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.platform.PermService
import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.common.util.InnerException
import github.afezeria.hymn.common.util.throwIfBucketNameInvalid
import github.afezeria.hymn.common.util.throwIfFileNameInvalid
import github.afezeria.hymn.oss.module.dto.FileRecordDto
import github.afezeria.hymn.oss.module.service.FileRecordService
import github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import github.afezeria.hymn.oss.web.controller.SimpleFileController
import mu.KLogging
import java.io.InputStream

/**
 * @author afezeria
 */
class OssServiceImpl(
    private val prefix: String,
    private val fileRecordService: FileRecordService,
    private val preSignedHistoryService: PreSignedHistoryService,
    private val dataBaseService: DataBaseService,
    private val permService: PermService,
    private val fileService: FileService
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
        contentType: String
    ): String {
        return dataBaseService.db().useTransaction {
            val id = fileRecordService.create(
                FileRecordDto(
                    bucket, objectName,
                    contentType = null,
                    path = objectName,
                    objectId = null,
                    fieldId = null,
                    dataId = null,
                    size = null,
                    remark = null,
                )
            )
            bucket.throwIfBucketNameInvalid()
            val b = prefix + bucket
            objectName.split('/').last().throwIfFileNameInvalid()
            fileService.putFile(b, objectName, inputStream, contentType)
            id
        }
    }

    override fun getObject(bucket: String, objectName: String, fn: (InputStream) -> Unit) {
        bucket.throwIfBucketNameInvalid()
        val b = prefix + bucket
        fileService.getFile(b, objectName, fn)
    }

    override fun moveObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        bucket.throwIfBucketNameInvalid()
        srcBucket.throwIfBucketNameInvalid()
        val b1 = prefix + bucket
        val b2 = prefix + srcBucket
        if (bucket == srcBucket && objectName == srcObjectName) {
            throw BusinessException("源对象和目标对象不能为同一个")
        }
        objectName.split('/').last().throwIfFileNameInvalid()
        fileService.moveFile(b1, objectName, b2, srcObjectName)

    }

    override fun copyObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ): String {
        bucket.throwIfBucketNameInvalid()
        srcBucket.throwIfBucketNameInvalid()
        val b1 = prefix + bucket
        val b2 = prefix + srcBucket

        if (bucket == srcBucket && objectName == srcObjectName) {
            throw BusinessException("源对象和目标对象不能为同一个")
        }
        val fileName = objectName.split('/').last()
        fileName.throwIfFileNameInvalid()
        return dataBaseService.db().useTransaction {
            val srcRecord = fileRecordService.findByBucketAndPath(bucket, objectName)
                ?: throw BusinessException("对象 bucket:$srcBucket,objectName:$srcObjectName 不存在")
            val newId = fileRecordService.create(
                FileRecordDto(
                    bucket = bucket,
                    fileName = fileName,
                    contentType = srcRecord.contentType,
                    path = objectName,
                    objectId = null,
                    fieldId = null,
                    dataId = null,
                    size = srcRecord.size,
                    remark = srcRecord.remark
                )
            )
            fileService.copyFile(
                b1,
                objectName.replace("$fileName$".toRegex(), "$newId-$fileName"),
                b2,
                srcObjectName
            )
            newId
        }
    }

    override fun getObjectUrl(bucket: String, objectName: String, expiry: Int): String {
        bucket.throwIfBucketNameInvalid()
        fileService.fileExist(prefix + bucket, objectName, null)

        logger.info("开始获取文件下载链接，文件路径： bucket: ${bucket}, objectName: $objectName")

        return if (fileService.remoteServerSupportHttpAccess()) {
            fileService.getFileUrl(prefix + bucket, objectName, expiry)
        } else {
            SimpleFileController.generatePreSignedFileUrl(bucket, objectName, expiry)
        }
    }

    override fun removeObject(bucket: String, objectName: String) {
        bucket.throwIfBucketNameInvalid()
        val b = prefix + bucket

        fileService.removeFile(b, objectName)
        fileRecordService.removeByBucketAndPath(bucket, objectName)
    }

    override fun objectExist(bucket: String, objectName: String): Boolean {
        bucket.throwIfBucketNameInvalid()
        return try {
            fileService.fileExist(prefix + bucket, objectName, null)
            true
        } catch (e: BusinessException) {
            false
        }
    }

    override fun getObjectWithPerm(objectId: String, fn: (InputStream) -> Unit) {
        TODO("Not yet implemented")
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
}