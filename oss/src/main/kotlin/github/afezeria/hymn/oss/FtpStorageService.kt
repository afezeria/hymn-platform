package github.afezeria.hymn.oss

import github.afezeria.hymn.common.platform.StorageService
import github.afezeria.hymn.oss.ftp.FTPClientFactory
import github.afezeria.hymn.oss.ftp.FTPClientPool
import github.afezeria.hymn.oss.ftp.FTPConfig
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPClientConfig
import org.apache.commons.net.ftp.FTPReply
import java.io.InputStream
import java.nio.file.Paths

/**
 * @author afezeria
 */
class FtpStorageService(config: FTPConfig) : StorageService {
    private val pool: FTPClientPool
    private val root: String

    init {
        val factory = FTPClientFactory(config)
        pool = FTPClientPool(factory)
        val client = pool.borrowObject()
        root = config.path ?: client.printWorkingDirectory()
    }

    private fun checkAndCreateDir(ftp: FTPClient, path: String) {
        val paths = Paths.get(path)

        val split = path.split("/")
        Paths.get(path)

    }

    override fun putFile(
        bucket: String,
        fileName: String,
        inputStream: InputStream,
        contentType: String
    ) {
        TODO("Not yet implemented")
    }

    override fun getFile(bucket: String, fileName: String): InputStream {
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

    override fun getFileUrl(bucket: String, fileName: String): String {
        TODO("Not yet implemented")
    }

    override fun removeFile(bucket: String, fileName: String) {
        TODO("Not yet implemented")
    }
}
