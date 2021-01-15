package github.afezeria.hymn.oss.ftp

import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.common.util.InnerException
import github.afezeria.hymn.oss.AbstractOssService
import github.afezeria.hymn.oss.web.controller.SimpleFileController
import mu.KLogging
import org.apache.commons.io.IOUtils
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.InputStream

/**
 * @author afezeria
 */
class FTPOssService(config: FTPConfig, private val controller: SimpleFileController) :
    AbstractOssService() {
    companion object : KLogging()

    private val pool: FTPClientPool
    private val root: String

    init {
        val factory = FTPClientFactory(config)
        pool = FTPClientPool(factory)
        val client = pool.borrowObject()
        root = config.path ?: client.printWorkingDirectory()
        root.trimEnd('/')
        prefix = config.prefix ?: ""
    }

    override fun isRemoteServerSupportHttpAccess(): Boolean {
        return false
    }

    override fun putFile(
        bucket: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String
    ) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            val path = "$root/$bucket/$objectName".replace("//", "/")

            logger.info("开始上传文件，远程路径:$path")

            ftp.createDirIfNotExist(path.substring(0, path.lastIndexOf('/')))

            for (i in 0..3) {
                val output = ftp.storeFileStream(path)
                if (!FTPReply.isPositivePreliminary(ftp.replyCode)) {
                    logger.warn("上传失败，replyString: {}", ftp.replyString)
                    throw InnerException("上传失败，replyString: ${ftp.replyString}")
                }
                output.use {
                    IOUtils.copy(inputStream, it)
                }
                if (ftp.completePendingCommand()) {
                    logger.info("上传完成")
                    return
                }
                if (FTPReply.isPositiveCompletion(ftp.replyCode)) {
                }
                logger.warn("上传失败，重试次数：{}，replyString: {}", ftp.replyString, i)
            }
        } finally {
            pool.returnObject(ftp)
        }
    }


    override fun getFile(bucket: String, objectName: String, fn: (InputStream) -> Unit) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            logger.info("开始下载文件，远程路径:$root/$bucket/$objectName")

            ftp.listNames("$root/$bucket/$objectName")
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("文件不存在")

            val inputStream = ftp.retrieveFileStream("$root/$bucket/$objectName")
            if (!FTPReply.isPositiveCompletion(ftp.replyCode)) {
                logger.warn("文件下载失败，replyString: ${ftp.replyString}")
                throw InnerException("文件下载失败，replyString: ${ftp.replyString}")
            }
            inputStream.use {
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
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        if (bucket == srcBucket && objectName == srcObjectName) {
            return
        }
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            val from = "$root/$srcBucket/$srcObjectName"
            val to = "$root/$bucket/$objectName"
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
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        if (bucket == srcBucket && objectName == srcObjectName) {
            return
        }
        var ftp1: FTPClient? = null
        try {
            ftp1 = pool.borrowObject()
            var ftp2: FTPClient? = null
            try {
                ftp2 = pool.borrowObject()
                val from = "$root/$srcBucket/$srcObjectName"
                val to = "$root/$bucket/$objectName"
                logger.info("开始复制文件，目标路径：$to ，源文件路径：$from")

                ftp1.listNames(to)
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw BusinessException("源文件不存在")

                ftp1.createDirIfNotExist("$root/$bucket")

                val inputStream = ftp1.retrieveFileStream(from)
                if (!FTPReply.isPositiveCompletion(ftp1.replyCode)) {
                    logger.warn("复制文件失败，replyCode:${ftp1.replyString}")
                    throw InnerException("复制文件失败，replyCode:${ftp1.replyString}")
                }
                val outputStream = ftp2.storeFileStream(to)

                if (!FTPReply.isPositiveCompletion(ftp2.replyCode)) {
                    logger.warn("复制文件失败，replyCode:${ftp2.replyString}")
                    throw InnerException("复制文件失败，replyCode:${ftp2.replyString}")
                }
                inputStream.use { i ->
                    outputStream.use { o ->
                        IOUtils.copy(i, o)
                    }
                }

                logger.info("复制文件成功")

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

    override fun getFileUrl(bucket: String, objectName: String, expiry: Int): String {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            logger.info("开始获取文件下载链接，文件路径：$root/$bucket/$objectName")

            ftp.listNames("$root/$bucket/$objectName")
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("文件不存在")

            return controller.generateFileUrl(bucket, objectName, expiry)
        } finally {
            pool.returnObject(ftp)
        }
    }

    override fun removeFile(bucket: String, objectName: String) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            val path = "$root/$bucket/$objectName"
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
        logger.debug("createDirIfNotExist [path:{}]", path)
        val cpath = path.trimEnd('/')
        val workDir = printWorkingDirectory()
        if (workDir == cpath) return
        if (workDir.startsWith(cpath)) return
        var currentDir = "/"
        val dirs: List<String>
        if (cpath.startsWith(workDir)) {
            if (workDir.length > 1) {
                currentDir = workDir.trimEnd('/')
            }
            dirs = cpath.removePrefix(workDir)
                .split('/').filter { it.isNotBlank() }
        } else {
            changeWorkingDirectory("/")
            dirs = cpath.split('/').filter { it.isNotBlank() }
        }
        logger.debug("current path: {}", currentDir)
        for (d in dirs) {
            currentDir += "$d/"
            var success = changeWorkingDirectory(d)
            if (!success) {
                logger.debug("path {} note exist, start create", currentDir)
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
