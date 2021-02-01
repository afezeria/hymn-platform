package github.afezeria.hymn.common.platform

import java.io.InputStream

/**
 * @author afezeria
 */
interface OssService {

    /**
     * 存储文件
     *
     * @param bucket bucket 名称
     * @param objectName 文件名
     * @param inputStream 文件的输入流
     * @param contentType http 的 MimeType 值
     * @param tmp 是否为临时文件，默认为 true
     * @return 文件id
     */
    fun putObject(
        bucket: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String,
        tmp: Boolean = true
    ): String

    /**
     *  获取文件字节流
     *
     * @param bucket bucket 名称
     * @param objectName 文件名名
     * @param fn 操作输入流的函数，在函数中完成数据的输出
     */
    fun getObject(bucket: String, objectName: String, fn: (InputStream) -> Unit)

    fun getObjectWithPerm(objectId: String, fn: (InputStream) -> Unit)
    fun getObject(objectId: String, fn: (InputStream) -> Unit)


    /**
     * 移动文件
     *
     * @param bucket bucket 名称
     * @param objectName 文件名称
     * @param srcBucket  源 bucket 名称
     * @param srcObjectName 源文件名称
     */
    fun moveObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    )

    /**
     * 复制文件
     *
     * @param bucket bucket 名称
     * @param objectName 文件名称
     * @param srcBucket  源 bucket 名称
     * @param srcObjectName 源文件名称
     */
    fun copyObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ): String


    /**
     *  获取文件的URL
     *
     * @param bucket bucket 名称
     * @param objectName 文件名称
     * @param expiry 有效时间，单位：秒
     */
    fun getObjectUrl(bucket: String, objectName: String, expiry: Int = 3600): String

    /**
     *  删除文件
     *
     * @param bucket bucket 名称
     * @param objectName 文件名称
     */
    fun removeObject(bucket: String, objectName: String)

    fun removeObjectWithPerm(objectId: String)
    fun removeObject(objectId: String)

    /**
     * 文件是否存在
     * @param bucket bucket 名称
     * @param objectName 文件名称
     */
    fun objectExist(bucket: String, objectName: String): Boolean

    /**
     * 根据id获取文件信息
     */
    fun getObjectInfoById(id: String): ObjectInfo?

    /**
     * 根据bucket获取文件列表
     * @param bucket
     * @param pageSize
     * @param pageNum
     * @return pair.first 文件id，pair.second 文件名
     */
    fun getObjectListByBucket(
        bucket: String,
        pageSize: Int,
        pageNum: Int
    ): List<Pair<String, String>>

}