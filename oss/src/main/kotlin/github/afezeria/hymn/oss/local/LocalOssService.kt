package github.afezeria.hymn.oss.local

import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.oss.AbstractOssService
import github.afezeria.hymn.oss.web.controller.SimpleFileController
import mu.KLogging
import org.apache.commons.io.IOUtils
import java.io.FileInputStream
import java.io.FileOutputStream
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
    private val controller: SimpleFileController,
    config: LocalConfig? = null,
) : AbstractOssService() {
    companion object : KLogging()

    val root: String

    init {
        root = config?.rootDir ?: System.getProperty("user.dir") + "/hymn_storage"
        val path = Paths.get(root)
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }
        prefix = config?.prefix ?: ""
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
        try {
            logger.info("开始上传文件，本地路径：$root/$bucket/$objectName")
            val dir = Paths.get("$root/$bucket")
            if (!Files.exists(dir)) {
                Files.createDirectories(dir)
            }
            IOUtils.copy(inputStream, FileOutputStream("$root/$bucket/$objectName"))
            logger.info("上传完成")
        } catch (e: IOException) {
            logger.info("上传失败")
            throw BusinessException("上传失败", e)
        }
    }


    override fun getFile(bucket: String, objectName: String, fn: (InputStream) -> Unit) {
        try {
            logger.info("开始下载文件，本地路径：$root/$bucket/$objectName")
            val dir = Paths.get("$root/$bucket")
            if (!Files.exists(dir)) {
                Files.createDirectories(dir)
            }
            FileInputStream("$root/$bucket/$objectName").use(fn)
            logger.info("下载完成")
        } catch (e: IOException) {
            logger.warn("文件下载失败，{}", e)
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
            Files.move(
                from, to,
                StandardCopyOption.REPLACE_EXISTING
            )
            logger.info("移动文件成功")
        } catch (e: IOException) {
            logger.warn("文件移动失败,{}", e)
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
            logger.info("开始复制文件，目标路径：$from，源文件路径：$to")
            if (!Files.exists(from)) {
                throw BusinessException("文件不存在")
            }
            Files.copy(
                from, to,
                StandardCopyOption.REPLACE_EXISTING
            )
            logger.info("复制文件成功")
        } catch (e: IOException) {
            logger.warn("复制文件失败,{}", e)
            throw BusinessException("复制文件失败", e)
        }

    }

    override fun getFileUrl(bucket: String, objectName: String, expiry: Int): String {
        logger.info("开始获取文件下载链接，文件路径：$root/$bucket/$objectName")
        if (!Files.exists(Path.of("$root/$bucket/$objectName"))) {
            throw BusinessException("文件不存在")
        }
        return controller.generateFileUrl(bucket, objectName, expiry)
    }

    override fun removeFile(bucket: String, objectName: String) {
        try {
            val path = Path.of("$root/$bucket/$objectName")
            logger.info("开始删除文件，路径：$path")
            if (Files.deleteIfExists(path)) {
                logger.info("删除文件成功")
            } else {
                logger.info("文件不存在")
            }
        } catch (e: IOException) {
            logger.warn("删除文件失败,{}", e)
            throw BusinessException("删除文件失败", e)
        }
    }
}