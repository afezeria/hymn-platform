package com.github.afezeria.hymn.common.platform

import com.github.afezeria.hymn.common.util.execute

/**
 * 基于数据库的缓存
 * @param group 组名，缓存的id由 group和key构成
 * @param expiry 过期时间，单位秒
 */
class DbCache(
    val group: String,
    val expiry: Long,
    private val databaseService: DatabaseService
) {
    /**
     * 根据pattern查询[group]中的值，pattern执行like匹配
     */
    fun get(pattern: String): List<Pair<String, String>> {
        val vs = mutableListOf<Pair<String, String>>()
        databaseService.db().useConnection {
            it.execute("select hymn.get_cache(?,?)", group, pattern) { rs ->
                if (rs != null) {
                    while (rs.next()) {
                        vs.add(
                            requireNotNull(rs.getString("c_key")) to
                                requireNotNull(rs.getString("c_value"))
                        )
                    }
                }
            }
        }
        return vs
    }

    /**
     * 设置值
     */
    fun set(key: String, value: String) {
        databaseService.primary().useConnection {
            it.execute("select hymn.set_cache(?,?,?,?)", group, key, value, expiry)
        }
    }

    /**
     * 根据key删除值
     */
    fun remove(key: String): Int {
        var count = 0
        databaseService.primary().useConnection {
            it.execute("select hymn.remove_cache(?,?)", group, key) {
                requireNotNull(it).apply {
                    next()
                    count = getInt(1)
                }
            }
        }
        return count
    }
}