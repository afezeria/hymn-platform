package github.afezeria.hymn.common.platform

import java.io.InputStream

/**
 * @author afezeria
 */
interface OssService {

    /**
     * 对象服务器是否支持直接http访问
     */
    fun remoteServerSupportHttpAccess(): Boolean


    /**
     * 存储对象
     *
     * @param bucket bucket 名称
     * @param objectName 对象名
     * @param inputStream 对象的输入流
     * @param contentType http 的 MimeType 值
     */
    fun putObject(bucket: String, objectName: String, inputStream: InputStream, contentType: String)

    /**
     *  获取对象字节流
     *
     * @param bucket bucket 名称
     * @param objectName 对象名名
     * @param fn 操作输入流的函数，在函数中完成数据的输出
     */
    fun getObject(bucket: String, objectName: String, fn: (InputStream) -> Unit)

    fun getObjectWithPerm(objectId: String, fn: (InputStream) -> Unit)


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
    )


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

    /**
     * 对象是否存在
     * @param bucket bucket 名称
     * @param objectName 对象名称
     */
    fun objectExist(bucket: String, objectName: String): Boolean

    fun getObjectListByBucket(bucket: String, page: Int, size: Int): List<String>

}