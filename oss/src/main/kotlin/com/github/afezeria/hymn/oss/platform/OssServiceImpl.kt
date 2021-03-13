package com.github.afezeria.hymn.oss.platform

import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.exception.PermissionDeniedException
import com.github.afezeria.hymn.common.platform.*
import com.github.afezeria.hymn.oss.OssConfigProperties
import com.github.afezeria.hymn.oss.StorageService
import com.github.afezeria.hymn.oss.module.dto.FileRecordDto
import com.github.afezeria.hymn.oss.module.dto.PreSignedHistoryDto
import com.github.afezeria.hymn.oss.module.entity.FileRecord
import com.github.afezeria.hymn.oss.module.service.FileRecordService
import com.github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import com.github.afezeria.hymn.oss.throwIfBucketNameInvalid
import com.github.afezeria.hymn.oss.throwIfFileNameInvalid
import com.github.afezeria.hymn.oss.web.controller.PreSignedUrlController
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream

/**
 * @author afezeria
 */
@Service
class OssServiceImpl(
    config: OssConfigProperties,
) : OssService {
    @Autowired
    private lateinit var fileRecordService: FileRecordService

    @Autowired
    private lateinit var preSignedHistoryService: PreSignedHistoryService

    @Autowired
    private lateinit var databaseService: DatabaseService

    @Autowired
    private lateinit var permService: PermService

    @Autowired
    private lateinit var storageService: StorageService

    @Autowired
    private lateinit var preSignedUrlController: PreSignedUrlController

    companion object : KLogging()

    private val prefix = config.prefix

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
        return databaseService.useTransaction {
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
                    tmp = tmp,
                )
            )
            val size = storageService.putFile(b, path, inputStream, contentType)
            fileRecordService.update(id, mapOf("size" to size.toInt()))
            id
        }
    }

    override fun getObject(
        bucket: String,
        objectName: String,
        fn: ResourceInfo.(InputStream) -> Unit
    ) {
        bucket.throwIfBucketNameInvalid()
        val record = fileRecordExist(bucket, objectName)

        val b = prefix + bucket
        storageService.getFile(b, objectName) { i -> fn(record.toObjectInfo(), i) }
    }

    override fun getObject(
        objectId: String,
        fn: ResourceInfo.(InputStream) -> Unit
    ) {
        val record =
            fileRecordService.findById(objectId) ?: throw DataNotFoundException("id:$objectId")
        val b = prefix + record.bucket
        storageService.getFile(b, record.path) { i -> fn(record.toObjectInfo(), i) }
    }


    override fun getObjectWithPerm(objectId: String, fn: ResourceInfo.(InputStream) -> Unit) {
        val record = fileRecordService.findById(objectId)
            ?: throw DataNotFoundException("id:$objectId")
        val func = { i: InputStream -> fn(record.toObjectInfo(), i) }
        record.apply {
            val bucket = prefix + bucket
            val accountType = Session.getInstance().accountType

            when (accountType) {
                AccountType.ANONYMOUS -> {
                    if (record.visibility != "anonymous")
                        throw PermissionDeniedException()
                }
                AccountType.NORMAL -> {
                    if (record.visibility == null) {
                        if (dataId == null
                            || this.objectId == null
                            || fieldId == null
                        ) {
                            throw PermissionDeniedException()
                        }
                        val oid = this.objectId!!
                        val fid = this.fieldId!!
                        val did = this.dataId!!
                        if (!(permService.hasObjectPerm(oid, query = true)
                                && permService.hasFieldPerm(oid, fid, read = true)
                                && permService.hasDataPerm(oid, did, read = true))
                        ) {
                            throw PermissionDeniedException()
                        }
                    }
                }
                AccountType.ADMIN -> {
                }
            }
            storageService.getFile(bucket, path, func)
        }
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
            throw BusinessException("源对象路径和目标对象路径不能为同一个")
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
            throw BusinessException("源对象路径和目标对象路径不能为同一个")
        }
        val b1 = prefix + bucket
        val b2 = prefix + srcBucket

        val fileName = objectName.split('/').last()
        fileName.throwIfFileNameInvalid()
        val srcRecord = fileRecordExist(srcBucket, srcObjectName)
        val newObjectName =
            objectName.replace(
                "$fileName$".toRegex(),
                "${System.currentTimeMillis()}-$fileName"
            )
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
        val record = fileRecordExist(bucket, objectName)
        storageService.fileExist(prefix + bucket, objectName, null)

        val accountType = Session.getInstance().accountType
        when (accountType) {
            AccountType.ANONYMOUS -> throw PermissionDeniedException()
            AccountType.NORMAL -> {
                if (record.dataId == null
                    || record.fieldId == null
                    || record.objectId == null
                ) {
                    throw PermissionDeniedException()
                } else {
                    val oid = record.objectId!!
                    val fid = record.fieldId!!
                    val did = record.dataId!!
                    if (!(permService.hasObjectPerm(oid, query = true)
                            && permService.hasFieldPerm(oid, fid, read = true)
                            && permService.hasDataPerm(oid, did, read = true))
                    ) {
                        throw PermissionDeniedException()
                    }
                }
            }
            AccountType.ADMIN -> {
            }
        }


        logger.info("开始获取对象下载链接，对象路径：bucket:$bucket, objectName:$objectName")

        val url = if (storageService.remoteServerSupportHttpAccess()) {
            storageService.getFileUrl(prefix + bucket, objectName, expiry)
        } else {
            preSignedUrlController.generatePreSignedObjectUrl(record.id, expiry.toLong())
        }
        preSignedHistoryService.create(PreSignedHistoryDto(fileId = record.id, expiry = expiry))
        return url
    }

    override fun removeObject(bucket: String, objectName: String): Int {
        bucket.throwIfBucketNameInvalid()
        val b = prefix + bucket

        storageService.removeFile(b, objectName)
        return fileRecordService.removeByBucketAndPath(bucket, objectName)
    }

    override fun removeObjectWithPerm(objectId: String): Int {
        val record = fileRecordService.findById(objectId) ?: return 0
        val accountType = Session.getInstance().accountType
        when (accountType) {
            AccountType.ANONYMOUS -> throw PermissionDeniedException()
            AccountType.NORMAL -> {
                if (record.dataId == null
                    || record.fieldId == null
                    || record.objectId == null
                ) {
                    throw PermissionDeniedException()
                } else {
                    val oid = record.objectId!!
                    val fid = record.fieldId!!
                    val did = record.dataId!!
                    if (!(permService.hasObjectPerm(oid, query = true, update = true)
                            && permService.hasFieldPerm(oid, fid, read = true, edit = true)
                            && permService.hasDataPerm(oid, did, read = true, update = true))
                    ) {
                        throw PermissionDeniedException()
                    }
                }
            }
            AccountType.ADMIN -> {
            }
        }

        storageService.removeFile(prefix + record.bucket, record.path)
        return fileRecordService.removeById(objectId)
    }

    override fun removeObject(objectId: String): Int {
        val record = fileRecordService.findById(objectId) ?: return 0
        storageService.removeFile(prefix + record.bucket, record.path)
        return fileRecordService.removeById(objectId)
    }

    override fun objectExist(bucket: String, objectName: String): Boolean {
        bucket.throwIfBucketNameInvalid()
        return try {
            fileRecordExist(bucket, objectName)
            storageService.fileExist(prefix + bucket, objectName, null)
            true
        } catch (e: DataNotFoundException) {
            false
        } catch (e: BusinessException) {
            false
        }
    }

    override fun getObjectInfoById(id: String): ResourceInfo? {
        return fileRecordService.findById(id)?.toObjectInfo()
    }


    override fun getObjectListByBucket(
        bucket: String,
        pageSize: Int,
        pageNum: Int
    ): List<ResourceInfo> {
        return fileRecordService.pageFindByBucket(bucket, pageSize, pageNum)
            .map { it.toObjectInfo() }
    }

    private fun fileRecordExist(bucket: String, objectName: String): FileRecord {
        return fileRecordService.findByBucketAndPath(bucket, objectName)
            ?: throw DataNotFoundException("文件记录 [bucket:$bucket, objectName:$objectName]")
    }
}