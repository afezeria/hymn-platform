package github.afezeria.hymn.common.platform

import java.io.InputStream

/**
 * @author afezeria
 */
interface StorageService {


    /**
     * 存放文件
     *
     * @param bucket  bucket  名称
     * @param fileName 文件名
     * @param inputStream 对象的输入流
     * @param contentType http 的 MimeType 值
     */
    fun putFile(bucket: String, fileName: String, inputStream: InputStream, contentType: String)

    /**
     *  获取文件流
     *
     * @param bucket  bucket  名称
     * @param fileName 文件名名
     */
    fun getFile(bucket: String, fileName: String): InputStream

    /**
     * 移动文件
     *
     * @param bucket  bucket  名称
     * @param fileName 文件名称
     * @param srcBucket  源 bucket 名称
     * @param srcFileName 源文件名称
     */
    fun moveFile(
        bucket: String,
        fileName: String,
        srcBucket: String,
        srcFileName: String
    )

    /**
     *  获取文件的URL
     *
     * @param bucket  bucket  名称
     * @param fileName 文件名称
     */
    fun getFileUrl(bucket: String, fileName: String): String

    /**
     *  删除文件
     *
     * @param bucket  bucket  名称
     * @param fileName 文件名称
     */
    fun removeFile(bucket: String, fileName: String)

}