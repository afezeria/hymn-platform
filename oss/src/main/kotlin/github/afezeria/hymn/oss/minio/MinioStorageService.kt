package github.afezeria.hymn.oss.minio

import github.afezeria.hymn.common.platform.StorageService
import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.oss.web.controller.SimpleFileController
import io.minio.*
import io.minio.errors.ErrorResponseException
import io.minio.http.Method
import mu.KLogging
import java.io.InputStream

/**
 * @author afezeria
 */
class MinioStorageService(config: MinioConfig, private val controller: SimpleFileController) :
    StorageService {
    companion object : KLogging()

    private val minioClient: MinioClient
    private val prefix: String
    private val useMinioPreSignedURL: Boolean

    init {
        minioClient = MinioClient.builder()
            .endpoint(config.url)
            .credentials(config.accessKey, config.secretKey)
            .build()
        prefix = config.prefix ?: ""
        useMinioPreSignedURL = config.useMinioPreSignedURL
    }

    override fun isRemoteServerSupportHttpAccess(): Boolean {
        return useMinioPreSignedURL
    }

    override fun putFile(
        bucket: String,
        fileName: String,
        inputStream: InputStream,
        contentType: String
    ) {
        val bucketName = prefix + bucket
        checkBucket(bucketName)
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(fileName)
                .stream(
                    inputStream,
                    -1,
                    10485760
                )
                .contentType(contentType).build()
        )
    }

    override fun getFile(bucket: String, fileName: String, fn: (InputStream) -> Unit) {
        try {
            checkBucket(prefix + bucket)
            minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(prefix + bucket)
                    .`object`(fileName)
                    .build()
            ).use {
                fn(it)
            }
        } catch (e: ErrorResponseException) {
            if (e.response().code() == 404) {
                logger.info("文件 $bucket/$fileName 不存在")
                throw BusinessException("文件不存在")
            }
            throw e
        }
    }

    override fun moveFile(
        bucket: String,
        fileName: String,
        srcBucket: String,
        srcFileName: String
    ) {
        if (bucket == srcBucket && fileName == srcFileName) {
            return
        }
        val bucketName = prefix + bucket
        val srcBucketName = prefix + srcBucket
        checkBucket(bucketName)
        checkBucket(srcBucketName)
        try {
            minioClient.copyObject(
                CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(fileName)
                    .source(
                        CopySource.builder()
                            .bucket(srcBucketName)
                            .`object`(srcFileName)
                            .build()
                    )
                    .build()
            )
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(srcBucketName)
                    .`object`(srcFileName)
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
        fileName: String,
        srcBucket: String,
        srcFileName: String
    ) {
        if (bucket == srcBucket && fileName == srcFileName) {
            return
        }
        val bucketName = prefix + bucket
        val srcBucketName = prefix + srcBucket
        checkBucket(bucketName)
        checkBucket(srcBucketName)
        try {
            minioClient.copyObject(
                CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(fileName)
                    .source(
                        CopySource.builder()
                            .bucket(srcBucketName)
                            .`object`(srcFileName)
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

    override fun getFileUrl(bucket: String, fileName: String, expiry: Int): String {
        val bucketName = prefix + bucket
        checkBucket(bucketName)
//        minioClient.statObject()
        return if (useMinioPreSignedURL) {
            minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .`object`(fileName)
                    .method(Method.GET)
                    .expiry(expiry)
                    .build()
            )
        } else {
            ""
        }
    }

    override fun removeFile(bucket: String, fileName: String) {
        val bucketName = prefix + bucket
        checkBucket(bucketName)
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .`object`(fileName)
                .build()
        )
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