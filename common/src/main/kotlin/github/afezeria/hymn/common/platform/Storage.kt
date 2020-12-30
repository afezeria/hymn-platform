package github.afezeria.hymn.common.platform

import java.io.InputStream

/**
 * @author afezeria
 */
interface Storage {


    /**
     * 存放文件
     *
     * @param bucketName  bucket  名称
     * @param fileName 自定义对象名称
     * @param inputStream 对象的输入流
     * @param contentType http 的 MimeType 值
     */
    fun putFile(bucketName: String, fileName: String, inputStream: InputStream, contentType: String)

    /**
     *  获取文件流
     *
     * @param bucketName the bucket name
     * @param fileName the object name
     * @return the file byte stream
     */
    fun getFile(bucketName: String, fileName: String): InputStream

    /**
     *  获取文件的URL
     *
     * @param bucketName the bucket name
     * @param fileName the object name
     * @return the file url
     */
    fun getFileUrl(bucketName: String, fileName: String): String

    /**
     *  删除文件
     *
     * @param bucketName the bucket name
     * @param fileName the object name
     */
    fun removeFile(bucketName: String, fileName: String)

}