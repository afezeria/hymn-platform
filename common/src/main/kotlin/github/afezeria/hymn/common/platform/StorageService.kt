package github.afezeria.hymn.common.platform

import java.io.InputStream

/**
 * @author afezeria
 */
interface StorageService {

    /**
     * 文件服务器是否支持直接http访问
     */
    fun isRemoteServerSupportHttpAccess(): Boolean


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
     * @param fn 操作输入流的函数，在函数中完成数据的输出
     */
    fun getFile(bucket: String, fileName: String, fn: (InputStream) -> Unit)


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
     * 复制文件
     *
     * @param bucket  bucket  名称
     * @param fileName 文件名称
     * @param srcBucket  源 bucket 名称
     * @param srcFileName 源文件名称
     */
    fun copyFile(
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
     * @param expiry 有效时间，单位：秒
     */
    fun getFileUrl(bucket: String, fileName: String, expiry: Int = 3600): String

    /**
     *  删除文件
     *
     * @param bucket  bucket  名称
     * @param fileName 文件名称
     */
    fun removeFile(bucket: String, fileName: String)

}