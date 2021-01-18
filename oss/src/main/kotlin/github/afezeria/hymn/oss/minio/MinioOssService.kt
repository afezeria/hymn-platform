package github.afezeria.hymn.oss.minio

import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.oss.AbstractOssService
import github.afezeria.hymn.oss.web.controller.SimpleFileController
import io.minio.*
import io.minio.errors.ErrorResponseException
import io.minio.http.Method
import mu.KLogging
import java.io.InputStream

/**
 * @author afezeria
 */
class MinioOssService(config: MinioConfig, controller: SimpleFileController) :
    AbstractOssService(prefix = config.prefix ?: "", controller) {
    companion object : KLogging()

    private val minioClient: MinioClient
    private val useMinioPreSignedURL: Boolean

    init {
        minioClient = MinioClient.builder()
            .endpoint(config.url)
            .credentials(config.accessKey, config.secretKey)
            .build()

        useMinioPreSignedURL = config.useMinioPreSignedURL
    }

    override fun remoteServerSupportHttpAccess(): Boolean {
        return !useMinioPreSignedURL
    }

    override fun putFile(
        bucket: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String
    ) {
        checkBucket(bucket)
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(objectName)
                .stream(
                    inputStream,
                    -1,
                    10485760
                )
                .contentType(contentType).build()
        )
    }

    override fun getFile(bucket: String, objectName: String, fn: (InputStream) -> Unit) {
        try {
            checkBucket(bucket)
            minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(objectName)
                    .build()
            ).use {
                fn(it)
            }
        } catch (e: ErrorResponseException) {
            if (e.response().code() == 404) {
                logger.info("文件 $bucket/$objectName 不存在")
                throw BusinessException("文件不存在")
            }
            throw e
        }
    }

    override fun moveFile(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        if (bucket == srcBucket && objectName == srcObjectName) {
            return
        }
        checkBucket(bucket)
        checkBucket(srcBucket)
        try {
            minioClient.copyObject(
                CopyObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(objectName)
                    .source(
                        CopySource.builder()
                            .bucket(srcBucket)
                            .`object`(srcObjectName)
                            .build()
                    )
                    .build()
            )
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(srcBucket)
                    .`object`(srcObjectName)
                    .build()
            )
        } catch (e: ErrorResponseException) {
            if (e.response().code() == 404) {
                throw BusinessException("文件不存在")
            }
            throw e
        }
    }

    override fun copyFile(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        if (bucket == srcBucket && objectName == srcObjectName) {
            return
        }
        checkBucket(bucket)
        checkBucket(srcBucket)
        try {
            minioClient.copyObject(
                CopyObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(objectName)
                    .source(
                        CopySource.builder()
                            .bucket(srcBucket)
                            .`object`(srcObjectName)
                            .build()
                    )
                    .build()
            )
        } catch (e: ErrorResponseException) {
            if (e.response().code() == 404) {
                throw BusinessException("文件不存在")
            }
            throw e
        }
    }

    override fun getFileUrl(bucket: String, objectName: String, expiry: Int): String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .bucket(bucket)
                .`object`(objectName)
                .method(Method.GET)
                .expiry(expiry)
                .build()
        )
    }

    override fun removeFile(bucket: String, objectName: String) {
        checkBucket(bucket)
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucket)
                .`object`(objectName)
                .build()
        )
    }

    override fun fileExist(bucket: String, objectName: String, client: Any?) {
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(objectName)
                    .build()
            )
        } catch (e: ErrorResponseException) {
            if (e.response().code() == 404) {
                throw BusinessException("文件不存在")
            }
            throw e
        }
    }

    private fun checkBucket(bucket: String) {
        val isExist: Boolean =
            minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build()
            )
        if (!isExist) {
            minioClient.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build()
            )
        }
    }
}