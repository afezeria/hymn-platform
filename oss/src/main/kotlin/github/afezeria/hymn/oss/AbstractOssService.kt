package github.afezeria.hymn.oss

import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.common.util.InnerException
import github.afezeria.hymn.common.util.throwIfBucketNameInvalid
import github.afezeria.hymn.common.util.throwIfFileNameInvalid
import java.io.InputStream

/**
 * @author afezeria
 */
abstract class AbstractOssService(val prefix: String = "") : OssService {
    init {
        if (!"([a-z][-a-z0-9]{0,9})?".toRegex().matches(prefix)) {
            throw InnerException("$prefix 不是有效的 bucket 前缀")
        }
    }

    override fun isRemoteServerSupportHttpAccess(): Boolean {
        return false
    }

    abstract fun putFile(
        bucket: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String
    )

    abstract fun getFile(bucket: String, objectName: String, fn: (InputStream) -> Unit)

    abstract fun moveFile(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    )

    abstract fun copyFile(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    )

    abstract fun getFileUrl(bucket: String, objectName: String, expiry: Int): String

    abstract fun removeFile(bucket: String, objectName: String)

    override fun putObject(
        bucket: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String
    ) {
        bucket.throwIfBucketNameInvalid()
        val b = prefix + bucket
        objectName.split('/').last().throwIfFileNameInvalid()
        putFile(b, objectName, inputStream, contentType)
    }

    override fun getObject(bucket: String, objectName: String, fn: (InputStream) -> Unit) {
        bucket.throwIfBucketNameInvalid()
        val b = prefix + bucket
        getFile(b, objectName, fn)
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
        moveFile(b1, objectName, b2, srcObjectName)
    }

    override fun copyObject(
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
        copyFile(b1, objectName, b2, srcObjectName)

    }

    override fun getObjectUrl(bucket: String, objectName: String, expiry: Int): String {
        bucket.throwIfBucketNameInvalid()
        val b = prefix + bucket

        return getFileUrl(b, objectName, expiry)
    }

    override fun removeObject(bucket: String, objectName: String) {
        bucket.throwIfBucketNameInvalid()
        val b = prefix + bucket

        removeFile(b, objectName)
    }
}