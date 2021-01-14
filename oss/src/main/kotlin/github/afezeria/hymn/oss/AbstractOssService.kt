package github.afezeria.hymn.oss

import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.util.throwIfBucketNameInvalid
import github.afezeria.hymn.common.util.throwIfFileNameInvalid
import java.io.InputStream

/**
 * @author afezeria
 */
abstract class AbstractOssService : OssService {
    protected var prefix: String = ""
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
        objectName.split('/').last().throwIfFileNameInvalid()
        putFile(prefix + bucket, objectName, inputStream, contentType)
    }

    override fun getObject(bucket: String, objectName: String, fn: (InputStream) -> Unit) {
        getFile(prefix + bucket, objectName, fn)
    }

    override fun moveObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        bucket.throwIfBucketNameInvalid()
        objectName.split('/').last().throwIfFileNameInvalid()
        moveFile(prefix + bucket, objectName, prefix + srcBucket, srcObjectName)
    }

    override fun copyObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        bucket.throwIfBucketNameInvalid()
        objectName.split('/').last().throwIfFileNameInvalid()
        copyFile(prefix + bucket, objectName, prefix + srcBucket, srcObjectName)
    }

    override fun getObjectUrl(bucket: String, objectName: String, expiry: Int): String {
        return getFileUrl(prefix + bucket, objectName, expiry)
    }

    override fun removeObject(bucket: String, objectName: String) {
        removeFile(prefix + bucket, objectName)
    }
}