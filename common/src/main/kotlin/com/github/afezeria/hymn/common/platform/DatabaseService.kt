package com.github.afezeria.hymn.common.platform

import org.ktorm.database.Database
import org.ktorm.database.Transaction

/**
 * 数据源接口
 * @author afezeria
 */
interface DatabaseService {

    fun db(): Database

    fun primary(): Database

    fun readOnly(): Database

    fun user(): Database

    fun <T> useTransaction(fn: (Transaction) -> T): T

    /**
     * 获取缓存操作对象
     * @param group 组名
     * @param expiry 过期时间，单位秒
     */
    fun getCache(group: String, expiry: Long): DbCache {
        return DbCache(group, expiry, this)
    }

}

