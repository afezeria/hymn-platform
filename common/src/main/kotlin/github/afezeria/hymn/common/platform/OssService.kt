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
     * @param objectName 对象名
     * @param fn 操作输入流的函数，在函数中完成数据的输出
     */
    fun getObject(bucket: String, objectName: String, fn: ResourceInfo.(InputStream) -> Unit)

    /**
     *  获取对象字节流，权限不满足时抛出异常
     *
     * @param objectId 对象id
     * @param fn 操作输入流的函数，在函数中完成数据的输出
     */
    fun getObjectWithPerm(objectId: String, fn: ResourceInfo.(InputStream) -> Unit)

    /**
     *  获取对象字节流，权限不满足时抛出异常
     *
     * @param objectId 对象id
     * @param fn 操作输入流的函数，在函数中完成数据的输出
     */
    fun getObject(objectId: String, fn: ResourceInfo.(InputStream) -> Unit)


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
    fun removeObject(bucket: String, objectName: String): Int

    /**
     *  删除对象，权限不满足时抛出异常
     *
     * @param objectId 对象id
     */
    fun removeObjectWithPerm(objectId: String): Int

    /**
     *  删除对象
     *
     * @param objectId 对象id
     */
    fun removeObject(objectId: String): Int

    /**
     * 对象是否存在
     * @param bucket bucket 名称
     * @param objectName 对象名称
     */
    fun objectExist(bucket: String, objectName: String): Boolean

    /**
     * 根据id获取对象信息
     */
    fun getObjectInfoById(id: String): ResourceInfo?

    /**
     * 根据bucket获取对象列表
     * @param bucket
     * @param pageSize
     * @param pageNum
     */
    fun getObjectListByBucket(
        bucket: String,
        pageSize: Int,
        pageNum: Int
    ): List<ResourceInfo>

}