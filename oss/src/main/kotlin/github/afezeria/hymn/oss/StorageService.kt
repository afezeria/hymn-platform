package github.afezeria.hymn.oss

import java.io.InputStream

/**
 * @author afezeria
 */
interface StorageService {

    fun remoteServerSupportHttpAccess(): Boolean {
        return false
    }

    fun putFile(
        bucket: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String
    ): Long

    fun getFile(bucket: String, objectName: String, fn: (InputStream) -> Unit)

    fun moveFile(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    )

    fun copyFile(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    )

    fun getFileUrl(bucket: String, objectName: String, expiry: Int): String

    fun removeFile(bucket: String, objectName: String)

    fun fileExist(bucket: String, objectName: String, client: Any? = null)

}
