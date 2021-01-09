package github.afezeria.hymn.oss

import github.afezeria.hymn.common.platform.StorageService
import java.io.InputStream

/**
 * 本地文件存储
 * @author afezeria
 */
class LocalStorageService(config: Map<String, Any?> = emptyMap()) : StorageService {
    override fun putFile(
        bucketName: String,
        fileName: String,
        inputStream: InputStream,
        contentType: String
    ) {
        TODO("Not yet implemented")
    }

    override fun getFile(bucketName: String, fileName: String): InputStream {
        TODO("Not yet implemented")
    }

    override fun moveFile(
        bucket: String,
        fileName: String,
        srcBucket: String,
        srcFileName: String
    ) {
        TODO("Not yet implemented")
    }

    override fun getFileUrl(bucketName: String, fileName: String): String {
        TODO("Not yet implemented")
    }

    override fun removeFile(bucketName: String, fileName: String) {
        TODO("Not yet implemented")
    }
}