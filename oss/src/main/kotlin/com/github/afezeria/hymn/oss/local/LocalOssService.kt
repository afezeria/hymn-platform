package com.github.afezeria.hymn.oss.local

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.oss.StorageService
import mu.KLogging
import org.apache.commons.io.IOUtils
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * 本地文件存储
 * @author afezeria
 */
class LocalOssService(
    config: LocalConfig? = null,
) : StorageService {
    companion object : KLogging()

    private val root: String

    init {
        root = config?.rootDir ?: (System.getProperty("user.dir") + "/hymn_storage")
        val path = Paths.get(root)
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }
    }

    override fun putFile(
        bucket: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String
    ): Long {
        try {
            val path = Path.of("$root/$bucket/$objectName")
            logger.info("开始上传文件，本地路径：$path")
            val dir = path.parent
            if (!Files.exists(dir)) {
                Files.createDirectories(dir)
            }

            val size = IOUtils.copyLarge(inputStream, path.toFile().outputStream())
            logger.info("上传完成")
            return size
        } catch (e: IOException) {
            logger.warn("上传失败", e)
            throw BusinessException("上传失败", e)
        }
    }


    override fun getFile(bucket: String, objectName: String, fn: (InputStream) -> Unit) {
        val path = Path.of("$root/$bucket/$objectName")
        try {
            logger.info("开始下载文件，本地路径：$path")

            FileInputStream(path.toFile()).use(fn)

            logger.info("下载完成")
        } catch (e: FileNotFoundException) {
            logger.warn("文件 {} 不存在", path)
            throw BusinessException("文件不存在")
        } catch (e: IOException) {
            logger.warn("文件下载失败", e)
            throw BusinessException("文件下载失败", e)
        }
    }

    override fun moveFile(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        try {
            val from = Path.of("$root/$srcBucket/$srcObjectName")
            val to = Path.of("$root/$bucket/$objectName")
            logger.info("开始移动文件，目标路径：$from，源文件路径：$to")
            if (!Files.exists(from)) {
                throw BusinessException("文件不存在")
            }

            val dir = to.parent
            if (!Files.exists(dir)) {
                Files.createDirectories(dir)
            }

            Files.move(
                from, to,
                StandardCopyOption.REPLACE_EXISTING
            )
            logger.info("移动完成")
        } catch (e: IOException) {
            logger.warn("文件移动失败", e)
            throw BusinessException("文件移动失败", e)
        }
    }

    override fun copyFile(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ) {
        try {
            val from = Path.of("$root/$srcBucket/$srcObjectName")
            val to = Path.of("$root/$bucket/$objectName")

            logger.info("开始复制文件，目标路径：$to ，源文件路径：$from")

            if (!Files.exists(from)) {
                throw BusinessException("文件不存在")
            }

            val dir = to.parent
            if (!Files.exists(dir)) {
                Files.createDirectories(dir)
            }

            Files.copy(
                from, to,
                StandardCopyOption.REPLACE_EXISTING
            )

            logger.info("复制完成")
        } catch (e: IOException) {
            logger.warn("复制文件失败", e)
            throw BusinessException("复制文件失败", e)
        }

    }

    override fun getFileUrl(bucket: String, objectName: String, expiry: Int): String {
        throw NotImplementedError()
    }

    override fun removeFile(bucket: String, objectName: String) {
        try {
            val path = Path.of("$root/$bucket/$objectName")
            logger.info("开始删除文件，路径：$path")
            if (Files.deleteIfExists(path)) {
                logger.info("删除完成")
            } else {
                logger.info("文件不存在")
            }
        } catch (e: IOException) {
            logger.warn("删除文件失败", e)
            throw BusinessException("删除文件失败", e)
        }
    }

    override fun fileExist(bucket: String, objectName: String, client: Any?) {
        if (!Files.exists(Path.of("$root/$bucket/$objectName"))) {
            throw BusinessException("文件不存在")
        }
    }
}