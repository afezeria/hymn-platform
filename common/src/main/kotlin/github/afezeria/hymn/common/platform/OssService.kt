package github.afezeria.hymn.common.platform

import java.io.InputStream

/**
 * @author afezeria
 */
interface OssService {

    /**
     * 存储对象
     *
     * @param bucket bucket 名称
     * @param objectName 对象名
     * @param inputStream 对象的输入流
     * @param contentType http 的 MimeType 值
     * @param tmp 是否为临时对象，默认为 true
     * @return 对象id
     */
    fun putObject(
        bucket: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String,
        tmp: Boolean = true
    ): String

    /**
     *  获取对象字节流
     *
     * @param bucket bucket 名称
     * @param objectName 对象名名
     * @param fn 操作输入流的函数，在函数中完成数据的输出
     */
    fun getObject(bucket: String, objectName: String, fn: (InputStream) -> Unit)

    fun getObjectWithPerm(objectId: String, fn: (InputStream) -> Unit)
    fun getObject(objectId: String, fn: (InputStream) -> Unit)


    /**
     * 移动对象
     *
     * @param bucket bucket 名称
     * @param objectName 对象名称
     * @param srcBucket  源 bucket 名称
     * @param srcObjectName 源对象名称
     */
    fun moveObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    )

    /**
     * 复制对象
     *
     * @param bucket bucket 名称
     * @param objectName 对象名称
     * @param srcBucket  源 bucket 名称
     * @param srcObjectName 源对象名称
     */
    fun copyObject(
        bucket: String,
        objectName: String,
        srcBucket: String,
        srcObjectName: String
    ): String


    /**
     *  获取对象的URL
     *
     * @param bucket bucket 名称
     * @param objectName 对象名称
     * @param expiry 有效时间，单位：秒
     */
    fun getObjectUrl(bucket: String, objectName: String, expiry: Int = 3600): String

    /**
     *  删除对象
     *
     * @param bucket bucket 名称
     * @param objectName 对象名称
     */
    fun removeObject(bucket: String, objectName: String)

    fun removeObjectWithPerm(objectId: String)
    fun removeObject(objectId: String)

    /**
     * 对象是否存在
     * @param bucket bucket 名称
     * @param objectName 对象名称
     */
    fun objectExist(bucket: String, objectName: String): Boolean

    /**
     * 根据bucket获取对象列表
     * @param bucket
     * @param pageSize
     * @param pageNum
     * @return pair.first 对象id，pair.second 对象名
     */
    fun getObjectListByBucket(
        bucket: String,
        pageSize: Int,
        pageNum: Int
    ): List<Pair<String, String>>

}