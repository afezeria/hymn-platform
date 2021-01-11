package github.afezeria.hymn.oss.ftp

import github.afezeria.hymn.common.platform.StorageService
import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.common.util.InnerException
import mu.KLogging
import org.apache.commons.io.IOUtils
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.InputStream

/**
 * @author afezeria
 */
class FtpStorageService(config: FTPConfig) : StorageService {
    companion object : KLogging()

    private val pool: FTPClientPool
    private val root: String

    init {
        val factory = FTPClientFactory(config)
        pool = FTPClientPool(factory)
        val client = pool.borrowObject()
        root = config.path ?: client.printWorkingDirectory()
        root.trimEnd('/')
    }

    override fun isRemoteServerSupportHttpAccess(): Boolean {
        return false
    }

    override fun putFile(
        bucket: String,
        fileName: String,
        inputStream: InputStream,
        contentType: String
    ) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            ftp.createDirIfNotExist("$root/$bucket")
            logger.info("start upload file, path:$root/$bucket/$fileName")
            for (i in 0..3) {
                ftp.storeFileStream("$root/$bucket/$fileName").use {
                    IOUtils.copy(inputStream, it)
                }
                if (FTPReply.isPositiveCompletion(ftp.replyCode)) {
                    logger.info("upload complete")
                    return
                }
                logger.warn("upload file failure! try uploading again... {} times", i)
            }
        } finally {
            pool.returnObject(ftp)
        }
    }


    override fun getFile(bucket: String, fileName: String, fn: (InputStream) -> Unit) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            ftp.listNames("$root/$bucket/$fileName")
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("file $root/$bucket/$fileName not exist")

            logger.info("start download file, path:$root/$bucket/$fileName")
            ftp.retrieveFileStream("$root/$bucket/$fileName").use {
                fn(it)
            }
            if (!FTPReply.isPositiveCompletion(ftp.replyCode)) {
                logger.warn("file download failure, replyCode:${ftp.replyString}")
            }
        } catch (e: Throwable) {
            logger.warn("file download failure", e)
            throw e
        } finally {
            pool.returnObject(ftp)
        }
    }

    override fun moveFile(
        bucket: String,
        fileName: String,
        srcBucket: String,
        srcFileName: String
    ) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            val from = "$root/$srcBucket/$srcFileName"
            val to = "$root/$bucket/$fileName"
            logger.info("start move file, path:$to , src path:$from")

            ftp.listNames(to)
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("source file $to not exist")

            ftp.createDirIfNotExist("$root/$bucket")
            ftp.rename(from, to)

            if (!FTPReply.isPositiveCompletion(ftp.replyCode)) {
                logger.warn("move file failure, replyCode:${ftp.replyString}")
            }
        } catch (e: Throwable) {
            logger.warn("move file failure", e)
            throw e
        } finally {
            pool.returnObject(ftp)
        }
    }

    override fun copyFile(
        bucket: String,
        fileName: String,
        srcBucket: String,
        srcFileName: String
    ) {
        var ftp1: FTPClient? = null
        try {
            ftp1 = pool.borrowObject()
            var ftp2: FTPClient? = null
            try {
                ftp2 = pool.borrowObject()
                val from = "$root/$srcBucket/$srcFileName"
                val to = "$root/$bucket/$fileName"
                logger.info("start copy file, path:$to , src path:$from")

                ftp1.listNames(to)
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw BusinessException("source file $to not exist")

                ftp1.createDirIfNotExist("$root/$bucket")

                ftp1.retrieveFileStream(from).use { i ->
                    ftp2.storeFileStream(to).use { o ->
                        IOUtils.copy(i, o)
                    }
                }

                if (!FTPReply.isPositiveCompletion(ftp1.replyCode)) {
                    logger.warn("copy file failure, replyCode:${ftp1.replyString}")
                }
            } finally {
                pool.returnObject(ftp2)
            }
        } catch (e: Throwable) {
            logger.warn("copy file failure", e)
            throw e
        } finally {
            pool.returnObject(ftp1)
        }
    }

    override fun getFileUrl(bucket: String, fileName: String): String {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            ftp.listNames("$root/$bucket/$fileName")
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("file $root/$bucket/$fileName not exist")
            return "/module/oss/public/file/$bucket/$fileName"
        } finally {
            pool.returnObject(ftp)
        }
    }

    override fun removeFile(bucket: String, fileName: String) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            val path = "$root/$bucket/$fileName"
            logger.info("start delete file, path:$path")
            ftp.listNames(path)
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("file $root/$bucket/$fileName not exist")

            if (!ftp.deleteFile(path)) {
                logger.warn("delete file failure, replyCode:${ftp.replyString}")
            }
        } finally {
            pool.returnObject(ftp)
        }
    }

    private fun FTPClient.createDirIfNotExist(path: String) {
        val cpath = path.trimEnd('/')
        val workDir = printWorkingDirectory()
        if (workDir == cpath) return
        if (workDir.startsWith(cpath)) return
        var currentDir = ""
        val dirs: List<String>
        if (cpath.startsWith(workDir)) {
            currentDir = workDir.trimEnd('/')
            dirs = cpath.removePrefix(workDir)
                .split('/').filter { it.isNotBlank() }
        } else {
            changeWorkingDirectory("/")
            dirs = cpath.split('/').filter { it.isNotBlank() }
        }
        for (d in dirs) {
            currentDir += "/$d"
            var success = changeWorkingDirectory(d)
            if (!success) {
                success = makeDirectory(d)
                if (!success) {
                    throw InnerException("failed to create $currentDir directory, replyCode:${replyString}")
                }
                success = changeWorkingDirectory(d)
                if (!success) {
                    throw InnerException("failed to change working directory to $currentDir, replyCode:${replyString}")
                }
            }
        }
        changeWorkingDirectory(workDir)
    }
}
