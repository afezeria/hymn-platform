package github.afezeria.hymn.oss.ftp

import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.common.util.InnerException
import github.afezeria.hymn.oss.FileService
import mu.KLogging
import org.apache.commons.io.IOUtils
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * @author afezeria
 */
class FTPOssService(config: FTPConfig) : FileService {
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
                    throw BusinessException("上传失败")
                }
                output.use {
                    IOUtils.copy(inputStream, it)
                }
                if (ftp.completePendingCommand()) {
                    logger.info("上传完成")
                    return
                }

                logger.warn("上传失败，重试次数：{}，replyString: {}", ftp.replyString, i)
            }
            throw BusinessException("上传失败")
        } finally {
            pool.returnObject(ftp)
        }
    }


    override fun getFile(bucket: String, objectName: String, fn: (InputStream) -> Unit) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()

            val path = "$root/$bucket/$objectName".replace("//", "/")

            logger.info("开始下载文件，远程路径:$path")

            fileExist(bucket, objectName, ftp)
//            ftp.listNames(path)
//                ?.takeIf { it.isNotEmpty() }
//                ?: throw BusinessException("文件不存在")

            val inputStream = ftp.retrieveFileStream(path)
            if (!FTPReply.isPositivePreliminary(ftp.replyCode)) {
                logger.warn("文件下载失败，replyString: ${ftp.replyString}")
                throw BusinessException("文件下载失败")
            }
            inputStream.use {
                fn(it)
            }
            if (ftp.completePendingCommand()) {
                logger.info("下载完成")
            } else {
                logger.warn("下载失败，replyString: ${ftp.replyString}")
            }
        } catch (e: IOException) {
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
            val from = "$root/$srcBucket/$srcObjectName".replace("//", "/")
            val to = "$root/$bucket/$objectName".replace("//", "/")
            logger.info("开始移动文件, 目标路径：$to , 源文件路径：$from")

            fileExist(srcBucket, srcObjectName, ftp)
//            ftp.listNames(from)
//                ?.takeIf { it.isNotEmpty() }
//                ?: throw BusinessException("源文件不存在")

            ftp.createDirIfNotExist(to.substring(0, to.lastIndexOf('/')))
            ftp.rename(from, to)

            if (!FTPReply.isPositiveCompletion(ftp.replyCode)) {
                logger.warn("移动文件失败，replyCode:${ftp.replyString}")
                throw BusinessException("移动文件失败")
            } else {
                logger.info("移动文件成功")
            }

        } catch (e: IOException) {
            throw BusinessException("移动文件失败", e)
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
                val from = "$root/$srcBucket/$srcObjectName".replace("//", "/")
                val to = "$root/$bucket/$objectName".replace("//", "/")

                logger.info("开始复制文件，目标路径：$to ，源文件路径：$from")

                fileExist(srcBucket, srcObjectName, ftp1)
//                ftp1.listNames(from)
//                    ?.takeIf { it.isNotEmpty() }
//                    ?: throw BusinessException("源文件不存在")

                ftp1.createDirIfNotExist(to.substring(0, to.lastIndexOf('/')))

                ftp1.retrieveFileStream(from)?.use { inputStream: InputStream ->
                    if (!FTPReply.isPositivePreliminary(ftp1.replyCode)) {
                        logger.warn("复制文件失败，ftp1 replyCode:${ftp1.replyString}")
                        throw BusinessException("复制文件失败")
                    } else {
                        ftp2.storeFileStream(to)?.use { outputStream: OutputStream? ->
                            if (!FTPReply.isPositivePreliminary(ftp2.replyCode)) {
                                logger.warn("复制文件失败，ftp2 replyCode:${ftp2.replyString}")
                                throw BusinessException("复制文件失败")
                            } else {
                                IOUtils.copy(inputStream, outputStream)
                            }
                        }
                    }

                }

                if (ftp1.completePendingCommand()) {
                    if (ftp2.completePendingCommand()) {
                        logger.info("复制文件成功")
                    } else {
                        logger.warn("复制文件失败，ftp2 replyCode:${ftp2.replyString}")
                        throw BusinessException("复制文件失败")
                    }
                } else {
                    logger.warn("复制文件失败，ftp1 replyCode:${ftp2.replyString}")
                    throw BusinessException("复制文件失败")
                }

            } finally {
                pool.returnObject(ftp2)
            }
        } catch (e: IOException) {
            throw BusinessException("复制文件失败", e)
        } finally {
            pool.returnObject(ftp1)
        }
    }

    override fun getFileUrl(bucket: String, objectName: String, expiry: Int): String {
        throw NotImplementedError()
    }

    override fun removeFile(bucket: String, objectName: String) {
        var ftp: FTPClient? = null
        try {
            ftp = pool.borrowObject()
            val path = "$root/$bucket/$objectName".replace("//", "/")
            logger.info("开始删除文件，路径：$path")

            fileExist(bucket, objectName, ftp)
//            val listNames = ftp.listNames(path)
//
//            if (listNames == null || listNames.isEmpty()) {
//                logger.info("文件不存在")
//                return
//            }

            if (!ftp.deleteFile(path)) {
                logger.warn("删除文件失败，replyCode:${ftp.replyString}")
                throw BusinessException("删除文件失败")
            } else {
                logger.info("删除文件成功")
            }
        } catch (e: IOException) {
            throw BusinessException("删除文件失败", e)
        } finally {
            pool.returnObject(ftp)
        }
    }

    override fun fileExist(bucket: String, objectName: String, client: Any?) {
        if (client != null) {
            val path = "$root/$bucket/$objectName".replace("//", "/")
            (client as FTPClient).listNames(path)
                ?.takeIf { it.isNotEmpty() }
                ?: throw BusinessException("文件不存在")
        } else {
            var ftp: FTPClient? = null
            try {
                ftp = pool.borrowObject()
                val path = "$root/$bucket/$objectName".replace("//", "/")
                ftp.listNames(path)
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw BusinessException("文件不存在")
            } finally {
                pool.returnObject(ftp)
            }
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
