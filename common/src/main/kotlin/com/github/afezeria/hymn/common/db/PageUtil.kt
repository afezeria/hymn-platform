package com.github.afezeria.hymn.common.db

/**
 *
 * @date 2021/6/28
 */
object PageUtil {
    internal val pageable = ThreadLocal<Triple<Int, Int, Boolean>?>()

    fun <E> pagination(pageNum: Int, pageSize: Int, query: () -> List<E>): Page<E> {
        if (pageNum < 1) throw IllegalArgumentException("pageNum must be greater than 0, current value $pageNum")
        if (pageSize < 1) throw IllegalArgumentException("pageSize must be greater than 0, current value $pageSize")
        val limit = pageSize
        val offset = pageSize * (pageNum - 1)
        pageable.set(Triple(offset, limit, true))
        val result: List<E>
        try {
            result = query.invoke()
        } finally {
            pageable.set(null)
        }
        @Suppress("UNCHECKED_CAST")
        return result as Page<E>
    }

    fun <R, E : List<R>> limit(offset: Int, limit: Int, query: () -> E): E {
        if (offset < 0) throw IllegalArgumentException("offset must be greater than or equal to 0, current value $offset")
        if (limit < 1) throw IllegalArgumentException("limit must be greater than 0, current value $limit")
        pageable.set(Triple(offset, limit, false))
        try {
            return query.invoke()
        } finally {
            pageable.set(null)
        }
    }
}