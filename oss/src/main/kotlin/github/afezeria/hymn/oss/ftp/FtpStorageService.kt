package github.afezeria.hymn.oss.ftp

import github.afezeria.hymn.common.platform.StorageService
import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.common.util.InnerException
import github.afezeria.hymn.oss.web.controller.SimpleFileController
import mu.KLogging
import org.apache.commons.io.IOUtils
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.InputStream

/**
 * @author afezeria
 */
class FtpStorageService(config: FTPConfig, private val controller: SimpleFileController) :
    StorageService {
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
            logger.info("开始上传文件，远程路径:$root/$bucket/$fileName")
            ftp.createDirIfNotExist("$root/$bucket")
            for (i in 0..3) {
                ftp.storeFileStream("$root/$bucket/$fileName").use {
                    IOUtils.copy(inputStream, it)
                }
                if (FTPReply.isPositiveCompletion(ftp.replyCode)) {
                    logger.info("上传完成")
                    return
                }
                logger.warn("上传失败！重试次数：{}，replyString: {}", ftp.replyString, i)
            }
        } finally {
            pool.returnObject(ftp)
        }
    }


    override fun getFile(bucket: String, fileName: String, fn: (InputStream) -> Unit) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            logger.info("开始下载文件，远程路径:$root/$bucket/$fileName")

            ftp.listNames("$root/$bucket/$fileName")
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("文件不存在")

            ftp.retrieveFileStream("$root/$bucket/$fileName").use {
                fn(it)
            }
            if (!FTPReply.isPositiveCompletion(ftp.replyCode)) {
                logger.warn("文件下载失败，replyString: ${ftp.replyString}")
            }
        } catch (e: Throwable) {
            logger.warn("文件下载失败，{}", e)
            throw BusinessException("文件下载失败", e)
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
        if (bucket == srcBucket && fileName == srcFileName) {
            return
        }
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            val from = "$root/$srcBucket/$srcFileName"
            val to = "$root/$bucket/$fileName"
            logger.info("开始移动文件, 目标路径：$to , 源文件路径：$from")

            ftp.listNames(from)
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("源文件不存在")

            ftp.createDirIfNotExist("$root/$bucket")
            ftp.rename(from, to)

            if (!FTPReply.isPositiveCompletion(ftp.replyCode)) {
                logger.warn("移动文件失败，replyCode:${ftp.replyString}")
            } else {
                logger.info("移动文件成功")
            }
        } catch (e: Throwable) {
            logger.warn("移动文件失败，{}", e)
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
        if (bucket == srcBucket && fileName == srcFileName) {
            return
        }
        var ftp1: FTPClient? = null
        try {
            ftp1 = pool.borrowObject()
            var ftp2: FTPClient? = null
            try {
                ftp2 = pool.borrowObject()
                val from = "$root/$srcBucket/$srcFileName"
                val to = "$root/$bucket/$fileName"
                logger.info("开始复制文件，目标路径：$to ，源文件路径：$from")

                ftp1.listNames(to)
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw BusinessException("源文件不存在")

                ftp1.createDirIfNotExist("$root/$bucket")

                ftp1.retrieveFileStream(from).use { i ->
                    ftp2.storeFileStream(to).use { o ->
                        IOUtils.copy(i, o)
                    }
                }

                if (!FTPReply.isPositiveCompletion(ftp1.replyCode)) {
                    logger.warn("复制文件失败，replyCode:${ftp1.replyString}")
                } else {
                    logger.info("复制文件成功")
                }

            } finally {
                pool.returnObject(ftp2)
            }
        } catch (e: Throwable) {
            logger.warn("复制文件失败，{}", e)
            throw e
        } finally {
            pool.returnObject(ftp1)
        }
    }

    override fun getFileUrl(bucket: String, fileName: String, expiry: Int): String {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            logger.info("开始获取文件下载链接，文件路径：$root/$bucket/$fileName")

            ftp.listNames("$root/$bucket/$fileName")
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("文件不存在")

            return controller.generateFileUrl(bucket, fileName, expiry)
        } finally {
            pool.returnObject(ftp)
        }
    }

    override fun removeFile(bucket: String, fileName: String) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            val path = "$root/$bucket/$fileName"
            logger.info("开始删除文件，路径：$path")
            val listNames = ftp.listNames(path)

            if (listNames == null || listNames.isEmpty()) {
                logger.info("文件不存在")
                return
            }

            if (!ftp.deleteFile(path)) {
                logger.warn("删除文件失败，replyCode:${ftp.replyString}")
            } else {
                logger.info("删除文件成功")
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
                    throw InnerException("创建远程目录 $currentDir 失败，replyCode:${replyString}")
                }
                success = changeWorkingDirectory(d)
                if (!success) {
                    throw InnerException("改变远程工作目录至 $currentDir 失败，replyCode:${replyString}")
                }
            }
        }
        changeWorkingDirectory(workDir)
    }
}
